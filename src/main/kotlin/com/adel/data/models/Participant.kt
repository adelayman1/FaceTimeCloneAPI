package com.adel.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Participant(
    var userEmail:String,
    var userName:String,
    var userId:String,
    var missedCall:Boolean = true,
    @BsonId
    var memberkey: Id<Participant>? = null
)