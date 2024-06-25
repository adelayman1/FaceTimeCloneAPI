package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class GetUserProfileByEmailUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String?): BaseResponse<UserModel> {
        if (email.isNullOrBlank())
            return BaseResponse.ErrorResponse(message = "user email is not valid")
        val getUserResult: UserModel = userRepository.getUserByEmail(email)
            ?: return BaseResponse.ErrorResponse(message = "user email is not exist")
       return BaseResponse.SuccessResponse(message = "user profile has got successfully", data = getUserResult)
    }
}