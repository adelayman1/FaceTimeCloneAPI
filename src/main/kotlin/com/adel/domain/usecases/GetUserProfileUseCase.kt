package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class GetUserProfileUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String?): BaseResponse<UserModel> {
        if (userId.isNullOrBlank())
            return BaseResponse.ErrorResponse(message = "userId is not valid")
        val getUserResult: UserModel = userRepository.getUserById(userId)
            ?: return BaseResponse.ErrorResponse(message = "user userId is not exist")
       return BaseResponse.SuccessResponse(message = "user profile has got successfully", data = getUserResult)
    }
}