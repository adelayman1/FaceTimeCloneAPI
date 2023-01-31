package com.adel.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Room(
    var roomType:Int,
    var roomAuthor:String,
    var time:String,
    var participants:List<Participant>?=null,
    @BsonId
    var roomId: Id<Room>? = null,
)