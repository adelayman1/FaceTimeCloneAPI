package com.adel.domain.usecases

import com.adel.data.models.CallInvitationDataModel
import com.adel.data.models.CallInvitationRequestModel
import com.adel.domain.models.RoomType
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository
import io.ktor.client.*

class SendCallInvitationUseCase constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        httpClient: HttpClient,
        userId: String,
        verified:Boolean,
        roomType: RoomType,
        roomId: String,
        participantsEmails: List<String>
    ): Boolean {
        return try {
            if (userId.isBlank())
                throw Exception("userId is not valid")
            if(!verified)
                throw Exception("user is not verified")
            // get participants Ids from participants emails and delete "Guest users"
            val participantsIds = participantsEmails.map {
                userRepository.getUserByEmail(it)?.fcmToken
                    ?: throw Exception("there is user in participants not found")
            }.filter { it != "Guest" }
            val createRoomResult = roomRepository.sendFcm(
                httpClient,
                CallInvitationRequestModel(
                    data = CallInvitationDataModel(
                        name = userRepository.getUserById(userId)?.userName ?: throw Exception("user not found"),
                        type = "call",
                        response = roomType.type,
                        key = roomId,
                        authorUID = userId
                    ), registration_ids = participantsIds
                )
            )
            createRoomResult
        } catch (e: Exception) {
            false
        }
    }
}