package com.adel.routes.user.requestsModels

data class CreateUserParams(var name: String, var email: String, var password: String,var fcmToken:String? = null)