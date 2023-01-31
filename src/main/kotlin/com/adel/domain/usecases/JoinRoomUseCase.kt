package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository
import io.ktor.http.*

class JoinRoomUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(userId: String,verified:Boolean, roomId: String): BaseResponse<RoomModel> {
        try {
            if(!verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            if (roomId.isBlank())
                return BaseResponse.ErrorResponse(message = "roomId is not valid")
            val roomInfoResult = roomRepository.getRoomInfo(roomId) ?: return BaseResponse.ErrorResponse(message = "Room not found")
            // if user is already the author
            if(roomInfoResult.roomAuthor == userId)
                return BaseResponse.SuccessResponse(
                    message = "user has joined successfully",
                    data = roomInfoResult
                )
            // if user isn't participant
            if(roomInfoResult.participants?.find { it.userId == userId } == null)
                BaseResponse.ErrorResponse(
                    message = "You don't have access to join room",
                    statusCode = HttpStatusCode.Forbidden
                )
            val joinRoomResult = roomRepository.joinRoom(userId, roomId)
            return if (joinRoomResult) {
                BaseResponse.SuccessResponse(
                    message = "user has joined successfully",
                    data = roomInfoResult
                )
            } else
                BaseResponse.ErrorResponse(message = "unknown error during join...")
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}