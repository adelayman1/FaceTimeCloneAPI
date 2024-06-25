package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class SendEmailVerifyCodeUseCase constructor(private val userRepository: UserRepository) {
    val password = System.getenv("email_password") ?: "password"
    suspend operator fun invoke(userId: String?): BaseResponse<String> {
        // TODO("FCM TOKEN UPDATE")
        try {
            if (userId.isNullOrBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            val userData = userRepository.getUserById(userId) ?: return BaseResponse.ErrorResponse(message = "userId is not exist")
            if (userData.isVerified)
                return BaseResponse.ErrorResponse(message = "user account is already verified")
            val otpCode = generateOtpCode()
            sendEmailToUser(userData, otpCode)
            val changeAccountDataResult = userRepository.changeAccountData(userId, otpCode = otpCode)
            return if (changeAccountDataResult)
                BaseResponse.SuccessResponse(message = "otp code sent successfully")
            else
                BaseResponse.ErrorResponse(message = "unknown error")
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }

    private suspend fun sendEmailToUser(userData: UserModel, otpCode: Int) {
        if (otpCode.toString().length != 4)
            throw Exception("otp is not valid")
        val emailForm = createEmailForm(userName = userData.userName, userEmail = userData.email, otpCode = otpCode)
        emailForm.send()
    }

    private suspend fun createEmailForm(userName: String, userEmail: String, otpCode: Int): SimpleEmail =
        withContext(Dispatchers.IO) {
            val emailForm = SimpleEmail()
            emailForm.hostName = "smtp.gmail.com"
            emailForm.setSmtpPort(465)
            emailForm.setAuthenticator(DefaultAuthenticator("adelayman0000@gmail.com", password))
            emailForm.isSSLOnConnect = true
            emailForm.setFrom("adelayman0000@gmail.com")
            emailForm.subject = "FaceTimeClone verify code"
            emailForm.setMsg(
                "Hello Mr ${userName}\n" +
                        "You registered an account on FaceTimeClone, before being able to use your account you need to verify that this is your email address by type this code in app: ${otpCode}\n" +
                        "\n" +
                        "Kind Regards, Adel Ayman"
            )
            emailForm.addTo(userEmail)
            return@withContext emailForm
        }

    private fun generateOtpCode() = (Math.random() * 9000).toInt() + 1000
}