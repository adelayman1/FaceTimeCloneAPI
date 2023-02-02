package com.adel.domain.usecases

import com.adel.data.utilities.Constants.FCM_GUEST_TOKEN
import com.adel.data.utilities.extensions.isEmailValid
import com.adel.data.utilities.extensions.isPasswordValid
import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class RegisterUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        fcmToken: String?
    ): BaseResponse<UserModel> {
        return try {
            if (name.isBlank() || name.length < 3)
                return BaseResponse.ErrorResponse(message = "Name is too short")
            if (!email.isEmailValid())
                return BaseResponse.ErrorResponse(message = "Email is not valid")
            if (!password.isPasswordValid())
                return BaseResponse.ErrorResponse(message = "Password is not valid")
            // check is email has been used before
            if (checkIsUserEmailUsed(email))
                return BaseResponse.ErrorResponse(message = "This email has been used before")
            val registerResult = userRepository.addUser(email, password, name, fcmToken ?: FCM_GUEST_TOKEN)
            return if (registerResult != null) {
                val accessToken = generateToken(userId = registerResult.userID, false)
                registerResult.userToken = accessToken
                BaseResponse.SuccessResponse(message = "Registration done successfully", data = registerResult)
            } else BaseResponse.ErrorResponse(message = "Unknown Error")
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = "unknown error ${e.message}")
        }
    }
    private suspend fun checkIsUserEmailUsed(email: String): Boolean = userRepository.getUserByEmail(email) != null

}