package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository

class GetUserRoomsUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(tokenData: TokenData): BaseResponse<List<RoomModel>> {
        try {
            if (tokenData.userId.isBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            if(!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            val getUserRoomsResult = roomRepository.getUserRooms(tokenData.userId)
            return BaseResponse.SuccessResponse(message = "rooms have got successfully", data = getUserRoomsResult)
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}