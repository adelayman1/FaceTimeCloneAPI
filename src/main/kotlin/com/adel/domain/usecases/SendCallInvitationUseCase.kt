package com.adel.domain.usecases

import com.adel.data.models.CallInvitationDataModel
import com.adel.data.models.CallInvitationRequestModel
import com.adel.data.models.TokenData
import com.adel.data.utilities.Constants.FCM_GUEST_TOKEN
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository

class SendCallInvitationUseCase constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        tokenData: TokenData,
        room: RoomModel
    ): Boolean {
        return try {
            if (tokenData.userId.isBlank())
                throw Exception("userId is not valid")
            if (!tokenData.verified)
                throw Exception("user is not verified")
            val participantsFcmTokens = getParticipantsFcmTokensByEmails(room.participants?.map { it.userEmail } ?: throw Exception("participants not found"))
            val sendFcmResult = roomRepository.sendFcm(
                CallInvitationRequestModel(
                    data = CallInvitationDataModel(
                        name = userRepository.getUserById(tokenData.userId)?.userName ?: throw Exception("user not found"),
                        type = "call",
                        response = room.roomType.type,
                        key = room.roomId,
                        authorUID = tokenData.userId
                    ), registration_ids = participantsFcmTokens
                )
            )
            sendFcmResult
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getParticipantsFcmTokensByEmails(participantsEmails: List<String>): List<String> {
        // get participants Ids from participants emails and delete "Guest users"
        return participantsEmails.map {
            userRepository.getUserByEmail(it)?.fcmToken
                ?: throw Exception("there is user in participants not found")
        }.filter { !isUserGuest(it) }
    }

    private fun isUserGuest(userFcm: String) = userFcm == FCM_GUEST_TOKEN

}