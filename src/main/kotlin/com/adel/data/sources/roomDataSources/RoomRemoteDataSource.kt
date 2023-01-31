package com.adel.data.sources.roomDataSources

import com.adel.data.models.Participant
import com.adel.data.models.Room
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.replaceOne
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.or

class RoomRemoteDataSource constructor(db: CoroutineDatabase) {
    private val rooms = db.getCollection<Room>("rooms")
    suspend fun createRoom(room: Room): Id<Room>? {
        val createRoomResult = rooms.insertOne(room)
        return room.roomId
    }

    suspend fun getRoomById(roomId: String): Room? {
        val bsonId: Id<Room> = ObjectId(roomId).toId()
        return rooms.findOne(Room::roomId eq bsonId)
    }

    suspend fun findRoomsByUserId(userId: String): List<Room> {
        // find rooms which passed user is author of it or participant
        return rooms.find(or(Room::roomAuthor eq userId,Room::participants/Participant::userId eq userId)).toList()
    }

    suspend fun updateRoom(roomId: String, newRoom: Room) =
        getRoomById(roomId)?.let { room ->
            val updateResult = rooms.replaceOne(
                room.copy(
                    roomType=newRoom.roomType,
                    roomAuthor = newRoom.roomAuthor,
                    participants = newRoom.participants,
                )
            )
            updateResult.modifiedCount == 1L
        } ?: false
//    suspend fun joinRoom(roomId: String, userId: String): Boolean = getRoomById(roomId)?.let { room ->
//        val newParticipants:List<Participant> = room.participants
//        newParticipants[userId] = room.participants[userId]!!.copy(missedCall = false)
//        val updateResult = rooms.replaceOne(
//            room.copy(
//                participants = newParticipants
//            )
//        )
//        updateResult.modifiedCount == 1L
//    } ?: false

    suspend fun deleteRoomById(roomId: String): Boolean {
        val createRoomResult = rooms.deleteOneById(ObjectId(roomId))
        return createRoomResult.deletedCount == 1L
    }
}