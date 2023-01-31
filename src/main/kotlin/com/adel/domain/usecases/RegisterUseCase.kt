package com.adel.domain.usecases

import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class RegisterUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, email: String, password: String,fcmToken:String?): BaseResponse<UserModel> {
        return try {
            //  check is name valid
            if (name.isBlank() || name.length < 3)
                return BaseResponse.ErrorResponse(message = "Name is too short")
            // check is email valid
            if (email.isBlank() || email.length < 5)
                return BaseResponse.ErrorResponse(message = "Email is too short")
            // check is password valid
            if (password.isBlank() || password.length < 5)
                return BaseResponse.ErrorResponse(message = "Password is too short")
            // check is email has been used before
            if (userRepository.getUserByEmail(email) != null)
                return BaseResponse.ErrorResponse(message = "This email has been used before")
            val registerResult = userRepository.addUser(email, password, name,fcmToken?:"Guest")
            return if (registerResult != null) {
                val accessToken = generateToken(userId = registerResult.userID,false)
                registerResult.userToken = accessToken
                BaseResponse.SuccessResponse(
                    message = "Registration done successfully",
                    data = registerResult
                )
            } else BaseResponse.ErrorResponse(message = "Unknown Error")
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = "unknown error ${e.message}")
        }
    }
}