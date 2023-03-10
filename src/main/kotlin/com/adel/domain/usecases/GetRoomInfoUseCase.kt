package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository
import io.ktor.http.*

class GetRoomInfoUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(tokenData: TokenData, roomId: String?): BaseResponse<RoomModel> {
        try {
            if (!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            if (roomId.isNullOrBlank())
                return BaseResponse.ErrorResponse(message = "roomId is not valid")
            val roomInfo: RoomModel = roomRepository.getRoomInfo(roomId) ?: return BaseResponse.ErrorResponse(message = "room is not exist")
            // check is user has access to view room information(is the author of room or participant)
            return if (checkIsUserHasAccess(tokenData.userId, roomId))
                BaseResponse.SuccessResponse(message = "room info has got successfully", data = roomInfo)
            else
                BaseResponse.ErrorResponse(message = "You don't have access to view room info", statusCode = HttpStatusCode.Forbidden)
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
    private suspend fun checkIsUserHasAccess(userId: String, roomId: String): Boolean {
        return roomRepository.checkIsUserAuthorOfRoom(userId, roomId) || roomRepository.checkIsUserParticipantInRoom(userId, roomId)
    }
}