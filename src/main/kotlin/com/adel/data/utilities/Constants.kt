package com.adel.data.utilities

object Constants {
    const val USERS_COLLECTION:String = "users"
    const val ROOMS_COLLECTION:String = "rooms"
    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    val FCM_AUTH_KEY = System.getenv("fcm_auth_key") ?: "secretKey"
    const val FCM_GUEST_TOKEN = "Guest"
}