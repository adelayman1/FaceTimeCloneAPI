package com.adel.domain.repositories

import com.adel.domain.models.UserModel

interface UserRepository {
    suspend fun addUser(email: String, password: String, name: String, fcmToken: String): UserModel?
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(userID: String): UserModel?
    suspend fun changeAccountData(
        userId: String,
        name: String?=null,
        email: String?=null,
        password: String?=null,
        fcmToken: String?=null,
        otpCode: Int?=null,
        verified: Boolean?=null
    ): Boolean
    suspend fun getOtpCode(userId: String): Int?
    suspend fun makeAccountVerified(userId: String): Boolean
    suspend fun deleteAccount(userId: String): Boolean
}