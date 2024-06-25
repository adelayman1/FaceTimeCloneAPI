package com.adel.data.sources.userDataSources

import com.adel.data.models.User
import com.adel.data.utilities.Constants.USERS_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.replaceOne
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId

class UserRemoteDataSource constructor(db:CoroutineDatabase) {
    private val users = db.getCollection<User>(USERS_COLLECTION)
    init {
        println("ahaaaaa")
        println("${users.find()}")
//        println("${users.collection.countDocuments()}")
        println("ahaaaaaa")
    }

     suspend fun insetUser(newUser: User): Id<User>? {
         users.insertOne(newUser)
         return newUser.userId
    }

     suspend fun getUserByEmail(email: String): User? {
         return users.findOne(User::email eq email)
    }
     suspend fun getUserById(userId: String): User? {
         val bsonId: Id<User> = ObjectId(userId).toId()
         return users.findOne(User::userId eq bsonId)
    }

     suspend fun updateUserDataById(newUserData: User): Boolean =
         getUserById(newUserData.userId.toString())?.let { user ->
            val updateResult = users.replaceOne(user.copy(name = newUserData.name , otpCode = newUserData.otpCode, verified = newUserData.verified, fcmToken = newUserData.fcmToken , email = newUserData.email , password = newUserData.password))
            updateResult.modifiedCount == 1L
        }?:false

     suspend fun deleteAccountById(userId: String): Boolean {
         val deleteResult = users.deleteOneById(ObjectId(userId))
        return deleteResult.deletedCount == 1L
    }
}