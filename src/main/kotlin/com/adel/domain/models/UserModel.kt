package com.adel.domain.models

data class UserModel(
    var userID: String = "-1",
    var userToken: String? = null,
    var userName: String,
    var email: String,
    var isVerified:Boolean= false,
    var fcmToken:String?=null,
)