package com.adel.data.repositories

import com.adel.data.models.User
import com.adel.data.sources.userDataSources.UserRemoteDataSource
import com.adel.data.utilities.extensions.toParticipantModel
import com.adel.data.utilities.extensions.toUserModel
import com.adel.domain.models.ParticipantModel
import com.adel.domain.models.RoomModel
import com.adel.domain.models.UserModel
import com.adel.domain.repositories.UserRepository

class UserRepositoryImpl constructor(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun addUser(email: String, password: String, name: String, fcmToken: String): UserModel? {
        val insertUserResult = userRemoteDataSource.insetUser(User(email = email, password = password, name = name, fcmToken = fcmToken))
        return userRemoteDataSource.getUserById(insertUserResult.toString())?.toUserModel()
    }

    override suspend fun getUserByEmail(email: String): UserModel? {
        return userRemoteDataSource.getUserByEmail(email)?.toUserModel()
    }

    override suspend fun getUserById(userID: String): UserModel? {
        return userRemoteDataSource.getUserById(userId = userID)?.toUserModel()
    }

    override suspend fun changeAccountData(
        userId: String,
        name: String?,
        email: String?,
        password: String?,
        fcmToken: String?,
        otpCode: Int?,
        verified: Boolean?
    ): Boolean {
        var getUserResult = userRemoteDataSource.getUserById(userId) ?: throw Exception("user not found")
        if (name != null)
            getUserResult = getUserResult.copy(name = name)
        if (email != null)
            getUserResult = getUserResult.copy(email = email)
        if (password != null)
            getUserResult = getUserResult.copy(password = password)
        if (fcmToken != null)
            getUserResult = getUserResult.copy(fcmToken = fcmToken)
        if (otpCode != null)
            getUserResult = getUserResult.copy(otpCode = otpCode)
        if (verified != null)
            getUserResult = getUserResult.copy(verified = verified)
        return userRemoteDataSource.updateUserDataById(userId, getUserResult)
    }

    override suspend fun getOtpCode(userId: String): Int? {
        val otpCode = userRemoteDataSource.getUserById(userId = userId)?.otpCode
        // return null if user hasn't requested verification message yet(otpCode = -1)
        return if (otpCode == -1) null else otpCode
    }

    override suspend fun makeAccountVerified(userId: String): Boolean {
        val userData = userRemoteDataSource.getUserById(userId) ?: return false
        val isVerified = userData.verified
        if (isVerified)
            return true
        return changeAccountData(userId = userId, verified = true)
    }

    override suspend fun getParticipantsFromEmails(participantsEmails:List<String>?):List<ParticipantModel> {
        return participantsEmails?.map {
            getUserByEmail(it)?.toParticipantModel(true)
                ?: throw Exception("there is user in participants not found")
        } ?: throw Exception("participants not found")
    }
    override suspend fun deleteAccount(userId: String): Boolean {
        return userRemoteDataSource.deleteAccountById(userId)
    }
}