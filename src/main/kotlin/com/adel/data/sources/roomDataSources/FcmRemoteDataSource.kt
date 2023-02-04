package com.adel.data.sources.roomDataSources

import com.adel.data.models.CallInvitationRequestModel
import com.adel.data.utilities.Constants.FCM_AUTH_KEY
import com.adel.data.utilities.Constants.FCM_BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FcmRemoteDataSource constructor(private val client: HttpClient){
    suspend fun fcmSend(callInvitationRequestModel: CallInvitationRequestModel) = withContext(Dispatchers.Default){
        client.post(FCM_BASE_URL) {
            contentType(ContentType.Application.Json)
            header("Authorization","key=${FCM_AUTH_KEY}")
            setBody(callInvitationRequestModel)
        }
    }
}