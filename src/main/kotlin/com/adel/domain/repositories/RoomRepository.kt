package com.adel.domain.repositories

import com.adel.data.models.CallInvitationRequestModel
import com.adel.data.models.Participant
import com.adel.domain.models.RoomModel

interface RoomRepository {
    suspend fun createRoom(roomData:RoomModel):String
    suspend fun getRoomInfo(roomId:String):RoomModel?
    suspend fun joinRoom(userId:String,roomId:String): Boolean
    suspend fun getUserRooms(userId:String): List<RoomModel>
    suspend fun deleteRoom(roomId: String): Boolean
    fun updateParticipantMissedCallState(participants: List<Participant>?, userId: String, isMissedCall:Boolean):List<Participant>?
    suspend fun sendFcm(callInvitationRequestModel: CallInvitationRequestModel): Boolean
    suspend fun checkIsUserAuthorOfRoom(userId: String,roomId: String):Boolean
    suspend fun checkIsUserParticipantInRoom(userId: String,roomId: String):Boolean
}