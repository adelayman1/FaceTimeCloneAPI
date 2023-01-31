package com.adel.domain.usecases

import com.adel.data.utilities.extensions.toParticipantModel
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.models.RoomType
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository
import io.ktor.client.*
import io.ktor.http.*

class CreateRoomUseCase constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val sendCallInvitationUseCase: SendCallInvitationUseCase
) {
    suspend operator fun invoke(
        httpClient: HttpClient,
        userId: String,
        verified: Boolean,
        roomType: RoomType,
        participantsEmails: List<String>?
    ): BaseResponse<RoomModel> {
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
            if (roomType != RoomType.LINK) {
                val participantsList = participantsEmails?.map {
                    userRepository.getUserByEmail(it)?.toParticipantModel(true)
                        ?: throw Exception("there is user in participants not found")
                } ?: throw Exception("participants not found")
                val createdRoomId = roomRepository.createRoom(roomType, userId, participantsList)
                val sendCallInvitationResult = sendCallInvitationUseCase(
                    httpClient = httpClient,
                    userId = userId,
                    verified = true,
                    roomType = roomType,
                    roomId = createdRoomId,
                    participantsEmails = participantsEmails
                )
                return if (sendCallInvitationResult) {
                    // when call invitation send to participants successfully
                    val createdRoomInfo = roomRepository.getRoomInfo(createdRoomId)
                    return if(createdRoomInfo != null)
                        BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoomInfo)
                    else
                        BaseResponse.ErrorResponse(message = "unknown error")
                } else {
                    // delete created room when couldn't send call invitation to participants
                    roomRepository.deleteRoom(createdRoomId)
                    BaseResponse.ErrorResponse(message = "unknown error during call...")
                }
            } else {
                val createRoomId = roomRepository.createRoom(roomType, userId, null)
                val createdRoomInfo = roomRepository.getRoomInfo(createRoomId)
                return if(createdRoomInfo != null)
                    BaseResponse.SuccessResponse(message = "room created successfully", data = createdRoomInfo)
                else
                    BaseResponse.ErrorResponse(message = "unknown error")
            }
        } catch (e: Exception) {
            return BaseResponse.ErrorResponse(message = "${e.message}")
        }
    }
}