package com.adel.domain.usecases

import com.adel.data.utilities.generateToken
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class VerifyCodeUseCase constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String?,otpCode:Int?): BaseResponse<UserModel> {
        if (userId.isNullOrBlank())
            return BaseResponse.ErrorResponse(message = "userId is not valid")
        val userData = userRepository.getUserById(userId)
            ?: return BaseResponse.ErrorResponse(message = "userId is not exist")
        if (userData.isVerified)
            return BaseResponse.ErrorResponse(message = "user account has already verified")
        if (!checkIsOtpCodeValid(otpCode))
            return BaseResponse.ErrorResponse(message = "otp code is not valid")
        val getOtpCodeResult = userRepository.getOtpCode(userId)
        return if(getOtpCodeResult == otpCode){
            userRepository.makeAccountVerified(userId)
            val accessToken = generateToken(userId = userId,true)
            userData.userToken = accessToken
            BaseResponse.SuccessResponse(message = "user account has verified successfully", data = userData)
        }else{
            BaseResponse.ErrorResponse(message = "code is not valid")
        }
    }
    private fun checkIsOtpCodeValid(otpCode: Int?): Boolean = otpCode == null || otpCode.toString().length<4
}