package com.adel.data.repositories

import com.adel.data.models.CallInvitationRequestModel
import com.adel.data.models.Participant
import com.adel.data.models.Room
import com.adel.data.sources.roomDataSources.FcmRemoteDataSource
import com.adel.data.sources.roomDataSources.RoomRemoteDataSource
import com.adel.data.utilities.extensions.fromParticipantsModel
import com.adel.data.utilities.extensions.toRoomModel
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.RoomRepository
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class RoomRepositoryImpl constructor(
    private val roomRemoteDataSource: RoomRemoteDataSource,
    private val fcmRemoteDataSource: FcmRemoteDataSource
) : RoomRepository {
//    roomType: RoomType, roomAuthor: String, participants: List<ParticipantModel>?
    override suspend fun createRoom(roomData:RoomModel) =
        withContext(Dispatchers.Default) {
            roomRemoteDataSource.createRoom(
                Room(
                    roomType = roomData.roomType.id,
                    roomAuthor = roomData.roomAuthor,
                    participants = roomData.participants?.map { it.fromParticipantsModel() },
                    time = LocalDateTime.now().toString()
                )
            ).toString()
        }
    override suspend fun getRoomInfo(roomId: String) = withContext(Dispatchers.Default) {
        roomRemoteDataSource.getRoomById(roomId)?.toRoomModel()
    }
    override suspend fun joinRoom(userId: String, roomId: String): Boolean {
        val getRoomResult = roomRemoteDataSource.getRoomById(roomId) ?: throw Exception("room not found")
        val newParticipants: List<Participant>? = getRoomResult.participants
        // update missedCall value for user which has joined
        newParticipants?.first { it.userId == userId }?.missedCall = false
        return roomRemoteDataSource.updateRoom(getRoomResult.copy(participants = newParticipants))
    }
    override suspend fun getUserRooms(userId: String): List<RoomModel> {
        val getUserRoomsResult = roomRemoteDataSource.findRoomsByUserId(userId)
        return getUserRoomsResult.map { it.toRoomModel() }
    }
    override suspend fun deleteRoom(roomId: String): Boolean {
        return roomRemoteDataSource.deleteRoomById(roomId)
    }
    override suspend fun checkIsUserAuthorOfRoom(userId: String,roomId: String):Boolean{
        val roomInfo: RoomModel = getRoomInfo(roomId)
            ?: throw Exception("room is not exist")
        return roomInfo.roomAuthor == userId
    }
    override suspend fun checkIsUserParticipantInRoom(userId: String,roomId: String):Boolean{
        val roomInfo: RoomModel = getRoomInfo(roomId)
            ?: throw Exception("room is not exist")
        return roomInfo.participants?.find { it.userId == userId } != null
    }
    override suspend fun sendFcm(callInvitationRequestModel: CallInvitationRequestModel): Boolean =
        withContext(Dispatchers.Default) {
            val fcmSendResult = fcmRemoteDataSource.fcmSend(callInvitationRequestModel)
            // check is fcm sent successfully
            fcmSendResult.call.response.status == HttpStatusCode.OK
        }
}