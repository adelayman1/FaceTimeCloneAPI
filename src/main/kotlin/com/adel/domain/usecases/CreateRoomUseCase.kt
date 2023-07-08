package com.adel.domain.usecases

import com.adel.data.models.TokenData
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.models.RoomType
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository
import com.adel.routes.rooms.requestModels.CreateRoomParams
import io.ktor.http.*

class CreateRoomUseCase constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val sendCallInvitationUseCase: SendCallInvitationUseCase
) {
    var userId: String? = null
    suspend operator fun invoke(tokenData: TokenData, roomData: CreateRoomParams): BaseResponse<RoomModel> {
        try {
            if (tokenData.userId.isBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            if (!tokenData.verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            userId = tokenData.userId

            if (checkIsParticipantsIsRequiredAndNotDefined(roomData))
                return BaseResponse.ErrorResponse(message = "participants are not found", statusCode = HttpStatusCode.NotFound)
            return if (roomData.roomType != RoomType.LINK) {
                val createdRoom = createFaceTimeRoom(roomData)
                BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoom)
            } else {
                val createdRoomInfo = createLinkRoom()
                BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoomInfo)
            }
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }

    // return true if room type [faceTime-audioCall] and participants are not defined
    private fun checkIsParticipantsIsRequiredAndNotDefined(roomData: CreateRoomParams) =
        roomData.roomType != RoomType.LINK && roomData.participantsEmails.isNullOrEmpty()

    private suspend fun createLinkRoom(): RoomModel {
        val roomData = RoomModel(
            roomType = RoomType.LINK,
            roomAuthor = userId ?: throw Exception("user not found"),
            participants = null
        )
        val createRoomId = roomRepository.createRoom(roomData)
        return checkIsRoomCreatedSuccessfully(createRoomId)
    }

    private suspend fun createFaceTimeRoom(roomData: CreateRoomParams): RoomModel {
        val participants = userRepository.getParticipantsFromEmails(roomData.participantsEmails)
        val roomModel = RoomModel(roomType = roomData.roomType, roomAuthor = userId ?: throw Exception("user not found"), participants = participants)
        val createdRoomId = roomRepository.createRoom(roomModel)
        val createdRoom = checkIsRoomCreatedSuccessfully(createdRoomId)
        val sendCallInvitationResult = sendCallInvitationToUsers(createdRoom)
        return if (sendCallInvitationResult)
            createdRoom
        else {
            // delete created room when couldn't send call invitation to participants
            roomRepository.deleteRoom(createdRoom.roomId)
            throw Exception("unknown error during call...")
        }
    }

    private suspend fun sendCallInvitationToUsers(createdRoom: RoomModel): Boolean {
        val sendCallInvitationResult =
            sendCallInvitationUseCase(tokenData = TokenData(userId = createdRoom.roomAuthor, true), createdRoom)
        return sendCallInvitationResult
    }

    private suspend fun checkIsRoomCreatedSuccessfully(roomId: String?): RoomModel {
        val createdRoomInfo = roomRepository.getRoomInfo(roomId ?: throw Exception("unknown error during create room"))
        if (createdRoomInfo != null)
            return createdRoomInfo
        else
            throw Exception("unknown error during create room")
    }
}