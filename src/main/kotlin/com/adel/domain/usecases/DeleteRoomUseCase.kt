package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.repositories.RoomRepository
import io.ktor.http.*

class DeleteRoomUseCase constructor(private val roomRepository: RoomRepository) {
    suspend operator fun invoke(tokenData: TokenData, roomId: String?): BaseResponse<Boolean> {
        try {
            if(!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            if (roomId.isNullOrBlank())
                return BaseResponse.ErrorResponse(message = "roomId is not valid")
            if (!roomRepository.checkIsUserAuthorOfRoom(tokenData.userId,roomId))
                return BaseResponse.ErrorResponse(message = "You don't have access to delete", statusCode = HttpStatusCode.Forbidden)
            return if (roomRepository.deleteRoom(roomId))
                BaseResponse.SuccessResponse(message = "room has deleted successfully", data = null)
            else
                BaseResponse.ErrorResponse(message = "unknown error")
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}