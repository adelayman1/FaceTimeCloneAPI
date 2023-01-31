package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.repositories.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeleteAccountUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String?): BaseResponse<Boolean> {
        try {
            if (userId.isNullOrBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            GlobalScope.launch {
                userRepository.deleteAccount(userId)
            }.join()
            return BaseResponse.SuccessResponse(message = "account has deleted successfully", data = null)
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}