package com.adel.data.utilities.extensions

import com.adel.data.models.Participant
import com.adel.data.models.Room
import com.adel.domain.models.ParticipantModel
import com.adel.domain.models.RoomModel
import com.adel.domain.models.RoomType

fun Participant.toParticipantsModel() = ParticipantModel(
    userEmail = userEmail,
    userName = userName,
    userId = userId,
    missedCall = missedCall
)
fun ParticipantModel.fromParticipantsModel() = Participant(
    userEmail = userEmail,
    userName = userName,
    userId = userId,
    missedCall = missedCall
)
fun Room.toRoomModel() = RoomModel(
    roomId = roomId.toString(),
    roomType = RoomType.valueOf(roomType)?:RoomType.LINK,
    roomAuthor = roomAuthor,
    participants = participants?.map { it.toParticipantsModel() },
    time = time
)