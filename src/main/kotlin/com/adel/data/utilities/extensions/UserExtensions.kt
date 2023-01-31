package com.adel.data.utilities.extensions

import com.adel.data.models.User
import com.adel.domain.models.ParticipantModel
import com.adel.domain.models.UserModel

fun User.toUserModel() = UserModel(
    userName = name,
    userID = userId.toString(),
    email = email,
    fcmToken = fcmToken,
    isVerified = verified
)
fun UserModel.toParticipantModel(missedCall:Boolean) = ParticipantModel(
    userName = userName,
    userEmail = email,
    userId = userID,
    missedCall = missedCall
)