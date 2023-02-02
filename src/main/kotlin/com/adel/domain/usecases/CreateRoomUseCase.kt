package com.adel.domain.usecases

import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.models.RoomType
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository
import io.ktor.http.*

class CreateRoomUseCase constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val sendCallInvitationUseCase: SendCallInvitationUseCase
) {
    suspend operator fun invoke(userId: String, verified: Boolean, roomType: RoomType, participantsEmails: List<String>?): BaseResponse<RoomModel> {
        try {
            if (userId.isBlank())
                return BaseResponse.ErrorResponse(message = "userId is not valid")
            if (!verified)
                return BaseResponse.ErrorResponse(message = "user is not verified")
            // return error if room type [faceTime-audioCall] and participants are not defined
            if (roomType != RoomType.LINK && participantsEmails.isNullOrEmpty())
                return BaseResponse.ErrorResponse(
                    message = "participants are not found",
                    statusCode = HttpStatusCode.NotFound
                )
            return if (roomType != RoomType.LINK) {
                val createdRoom = createFaceTimeRoom(roomType,userId,participantsEmails)
                BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoom)
            } else {
                val createdRoomInfo = createLinkRoom(userId)
                BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoomInfo)
            }
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
    private suspend fun createLinkRoom(userId:String):RoomModel {
        val createRoomId = roomRepository.createRoom(RoomType.LINK, userId, null)
        return checkIsRoomCreatedSuccessfully(createRoomId)
    }
    private suspend fun createFaceTimeRoom(roomType: RoomType, userId:String, participantsEmails: List<String>?):RoomModel {
        val participants = userRepository.getParticipantsFromEmails(participantsEmails)
        val createdRoomId = roomRepository.createRoom(roomType, userId, participants)
        val createdRoom = checkIsRoomCreatedSuccessfully(createdRoomId)
        val sendCallInvitationResult = sendCallInvitationToUsers(createdRoom,participantsEmails!!)
        return if(sendCallInvitationResult)
            createdRoom
        else
            throw Exception("unknown error during call...")
    }
    private suspend fun checkIsRoomCreatedSuccessfully(roomId:String?):RoomModel {
        val createdRoomInfo = roomRepository.getRoomInfo(roomId ?: throw Exception("unknown error during create room"))
        if(createdRoomInfo != null)
            return createdRoomInfo
        else
            throw Exception("unknown error during create room")
    }
    private suspend fun sendCallInvitationToUsers(createdRoom:RoomModel,participantsEmails: List<String>):Boolean {
        val sendCallInvitationResult = sendCallInvitationUseCase(
            userId = createdRoom.roomAuthor,
            verified = true,
            roomType = createdRoom.roomType,
            roomId = createdRoom.roomId,
            participantsEmails = participantsEmails
        )
        return if (sendCallInvitationResult) {
            true
        } else {
            // delete created room when couldn't send call invitation to participants
            roomRepository.deleteRoom(createdRoom.roomId)
            false
        }
    }
}