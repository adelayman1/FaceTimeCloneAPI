package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class VerifyCodeUseCase constructor(private val userRepository: UserRepository) {
    var userId: String? = null
    suspend operator fun invoke(userId: String?, otpCode: Int?): BaseResponse<UserModel> {
        return try {
            if (userId.isNullOrBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            val userData = userRepository.getUserById(userId) ?: return BaseResponse.ErrorResponse(message = "userId is not exist")
            if (userData.isVerified)
                return BaseResponse.ErrorResponse(message = "user account has already verified")
            this.userId = userId
            return if (!checkIsOtpCodeValid(otpCode))
                BaseResponse.ErrorResponse(message = "otp code is not valid")
            else {
                userRepository.makeAccountVerified(userId)
                val accessToken = generateToken(TokenData(userId = userId, true))
                userData.userToken = accessToken
                BaseResponse.SuccessResponse(message = "user account has verified successfully", data = userData)
            }
        } catch (e: Exception) {
            BaseResponse.ErrorResponse(message = e.message.toString())
        }
    }
    private suspend fun checkIsOtpCodeValid(otpCode: Int?): Boolean {
        val getOtpCodeResult = userRepository.getOtpCode(userId ?: throw Exception("user not found"))
        return otpCode != null && otpCode.toString().length == 4 && getOtpCodeResult == otpCode
    }
}