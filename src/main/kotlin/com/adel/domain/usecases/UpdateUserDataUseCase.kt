package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UpdateUserDataUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(tokenData: TokenData, name: String?=null, userFcmToken: String?=null): BaseResponse<UserModel> {
        return try {
            if (tokenData.userId.isBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            if(!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            if (userRepository.getUserById(tokenData.userId) == null)
                return BaseResponse.ErrorResponse(message = "user is not exist")

            var result: UserModel? = null
            runBlocking {
                if (!name.isNullOrEmpty()) {
                    withContext(Dispatchers.Default) {
                        userRepository.changeAccountData(
                            tokenData.userId,
                            name
                        )
                    }
                }
                if (userFcmToken != null) {
                    withContext(Dispatchers.Default) {
                        userRepository.changeAccountData(
                            userId = tokenData.userId,
                            fcmToken = userFcmToken
                        )
                    }
                }
                result = userRepository.getUserById(tokenData.userId)
            }
            return if (result == null) BaseResponse.ErrorResponse(message = "please change user data first")
            else BaseResponse.SuccessResponse(message = "user details updated successfully", data = result)
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = "unknown error ${e.message}")
        }
    }
}