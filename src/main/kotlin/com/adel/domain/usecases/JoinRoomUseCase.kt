package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository
import io.ktor.http.*

class JoinRoomUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(tokenData: TokenData, roomId: String): BaseResponse<RoomModel> {
        try {
            if(!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            if (roomId.isBlank())
                return BaseResponse.ErrorResponse(message = "roomId is not valid")
            val roomInfoResult = roomRepository.getRoomInfo(roomId) ?: return BaseResponse.ErrorResponse(message = "Room not found")
            // if user is already the author
            if(roomRepository.checkIsUserAuthorOfRoom(tokenData.userId, roomId))
                return BaseResponse.SuccessResponse(message = "user has joined successfully", data = roomInfoResult)
            // if user isn't participant
            if(!roomRepository.checkIsUserParticipantInRoom(tokenData.userId, roomId))
                BaseResponse.ErrorResponse<RoomModel>(message = "You don't have access to join room", statusCode = HttpStatusCode.Forbidden)
            val joinRoomResult = roomRepository.joinRoom(tokenData.userId, roomId)
            return if (joinRoomResult) {
                BaseResponse.SuccessResponse(message = "user has joined successfully", data = roomInfoResult)
            } else
                BaseResponse.ErrorResponse(message = "unknown error during join...")
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}