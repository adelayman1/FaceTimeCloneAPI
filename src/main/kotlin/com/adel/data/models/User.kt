package com.adel.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class User(
    var name:String,
    val email:String,
    val password:String,
    val otpCode:Int = -1,
    val verified:Boolean = false,
    val fcmToken:String,
    @BsonId
    var userId: Id<User>? = null,
)