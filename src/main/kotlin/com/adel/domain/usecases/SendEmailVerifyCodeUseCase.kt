package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.repositories.UserRepository
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class SendEmailVerifyCodeUseCase constructor(private val userRepository: UserRepository) {
    val password = System.getenv("email_password") ?: "password"
    suspend operator fun invoke(userId: String?): BaseResponse<Any> {
        if (userId.isNullOrBlank())
            return BaseResponse.ErrorResponse(message = "userId is not valid")
        val userData = userRepository.getUserById(userId)
            ?: return BaseResponse.ErrorResponse(message = "userId is not exist")
        if (userData.isVerified)
            return BaseResponse.ErrorResponse(message = "user account is already verified")
        val otpCode = (Math.random() * 9000).toInt() + 1000
        val emailForm = SimpleEmail()
        emailForm.hostName = "smtp.gmail.com"
        emailForm.setSmtpPort(465)
        emailForm.setAuthenticator(DefaultAuthenticator("adelayman0000@gmail.com", password))
        emailForm.isSSLOnConnect = true
        emailForm.setFrom("adelayman0000@gmail.com")
        emailForm.subject = "FaceTimeClone verify code"
        emailForm.setMsg(
            "Hello Mr ${userData.userName}\n" +
                    "You registered an account on FaceTimeClone, before being able to use your account you need to verify that this is your email address by type this code in app: ${otpCode}\n" +
                    "\n" +
                    "Kind Regards, Adel Ayman"
        )
        emailForm.addTo(userData.email)
        emailForm.send()
        return if (userRepository.changeAccountData(userId, otpCode = otpCode))
            BaseResponse.SuccessResponse(message = "otp code has sent successfully")
        else
            BaseResponse.ErrorResponse(message = "unknown error")
    }
}