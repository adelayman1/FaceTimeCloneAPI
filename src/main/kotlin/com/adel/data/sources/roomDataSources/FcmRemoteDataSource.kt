package com.adel.data.sources.roomDataSources

import com.adel.data.models.CallInvitationRequestModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FcmRemoteDataSource {
    companion object{
        private const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
        private val AUTH_KEY = System.getenv("fcm_auth_key") ?: "secretKey"
    }

    suspend fun fcmSend(client:HttpClient,callInvitationRequestModel: CallInvitationRequestModel) = withContext(Dispatchers.Default){
        client.post(FCM_BASE_URL) {
            contentType(ContentType.Application.Json)
            header("Authorization","key=${AUTH_KEY}")
            setBody(callInvitationRequestModel)
        }
    }
}