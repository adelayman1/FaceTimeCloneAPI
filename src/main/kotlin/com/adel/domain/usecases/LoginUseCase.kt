package com.adel.domain.usecases

import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class LoginUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): BaseResponse<UserModel> {
        return try {
            // check is email valid
            if (email.isBlank() || email.length < 5)
                return BaseResponse.ErrorResponse(message = "Email is too short")
            // check is password valid
            if (password.isBlank() || password.length < 5)
                return BaseResponse.ErrorResponse(message = "Password is too short")
            // get user with given data
            val loginResult = userRepository.getUserByEmail(email)
            if(loginResult!=null){
                val accessToken = generateToken(userId = loginResult.userID,loginResult.isVerified)
                loginResult.userToken = accessToken
                BaseResponse.SuccessResponse(
                    message = "Login done successfully",
                    data = loginResult
                )
                return BaseResponse.SuccessResponse(message = "Login done successfully", data = loginResult)
            }else{
                return BaseResponse.ErrorResponse(message = "Email is not exist") 
            }
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}