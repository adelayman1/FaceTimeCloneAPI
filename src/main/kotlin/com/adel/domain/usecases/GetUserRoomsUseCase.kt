package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository

class GetUserRoomsUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(userId: String,verified:Boolean): BaseResponse<List<RoomModel>> {
        try {
            if (userId.isBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            if(!verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            val getUserRoomsResult = roomRepository.getUserRooms(userId)
            return BaseResponse.SuccessResponse(message = "rooms have got successfully", data = getUserRoomsResult)
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}