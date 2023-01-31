package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class GetUserFcmTokenUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String,verified:Boolean): BaseResponse<String> {
        if(!verified)
            return BaseResponse.ErrorResponse(message = "user is not verified")
        if (userId.isBlank())
            return BaseResponse.ErrorResponse(message = "userId is not valid")
        val getUserResult: UserModel = userRepository.getUserById(userId)
            ?: return BaseResponse.ErrorResponse(message = "user email is not exist")
        return if(getUserResult.fcmToken.isNullOrBlank())
            BaseResponse.ErrorResponse(message = "token is not added")
        else
            BaseResponse.SuccessResponse(message = "token has got successfully", data = getUserResult.fcmToken)
    }
}