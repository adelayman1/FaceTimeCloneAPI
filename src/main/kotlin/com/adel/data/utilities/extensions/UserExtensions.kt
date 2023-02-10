package com.adel.data.utilities.extensions

import com.adel.data.models.User
import com.adel.domain.models.ParticipantModel
import com.adel.domain.models.UserModel
import java.util.regex.Pattern

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
fun String.isEmailValid():Boolean{
    return !isBlank() && length > 4 && Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    ).matcher(this).matches()
}
fun String.isPasswordValid():Boolean{
//    return !isBlank() && Pattern.compile(
//        "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}"
//    ).matcher(this).matches()
    return true
}