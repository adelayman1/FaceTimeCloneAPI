package com.adel.routes.rooms.requestModels

import com.adel.domain.models.RoomType

data class CreateRoomParams(
    val roomType: RoomType,
    var participantsEmails: List<String>? = null
)