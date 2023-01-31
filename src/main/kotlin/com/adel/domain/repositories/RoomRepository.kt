package com.adel.domain.repositories

import com.adel.data.models.CallInvitationRequestModel
import com.adel.domain.models.ParticipantModel
import com.adel.domain.models.RoomModel
import com.adel.domain.models.RoomType
import io.ktor.client.*

interface RoomRepository {
    suspend fun createRoom(roomType:RoomType,roomAuthor:String,participants:List<ParticipantModel>?):String
    suspend fun getRoomInfo(roomId:String):RoomModel?
    suspend fun joinRoom(userId:String,roomId:String): Boolean
    suspend fun getUserRooms(userId:String): List<RoomModel>
    suspend fun deleteRoom(roomId: String): Boolean
    suspend fun sendFcm(client: HttpClient, callInvitationRequestModel: CallInvitationRequestModel): Boolean
}