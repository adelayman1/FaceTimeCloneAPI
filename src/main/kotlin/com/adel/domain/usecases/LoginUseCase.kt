package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.data.utilities.extensions.isEmailValid
import com.adel.data.utilities.extensions.isPasswordValid
import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class LoginUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): BaseResponse<UserModel> {
         try {
            if (!email.isEmailValid())
                return BaseResponse.ErrorResponse(message = "Email is not valid")
            if (!password.isPasswordValid())
                return BaseResponse.ErrorResponse(message = "Password is not valid")
            val loginResult = userRepository.getUserByEmail(email)
             return if(loginResult!=null){
                 val accessToken = generateToken(TokenData(userId = loginResult.userID,verified = loginResult.isVerified))
                 loginResult.userToken = accessToken
                 BaseResponse.SuccessResponse(
                     message = "Login done successfully",
                     data = loginResult
                 )
                 BaseResponse.SuccessResponse(message = "Login done successfully", data = loginResult)
             }else{
                 BaseResponse.ErrorResponse(message = "Email is not exist")
             }
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}