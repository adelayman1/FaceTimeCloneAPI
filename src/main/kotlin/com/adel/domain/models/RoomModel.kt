package com.adel.domain.models

data class RoomModel(
    var roomId: String = "-1",
    var roomType: RoomType,
    var roomAuthor: String,
    var participants: List<ParticipantModel>?,
    var time: String,
)