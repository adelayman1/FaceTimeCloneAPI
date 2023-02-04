package com.adel.di.modules

import com.adel.data.repositories.RoomRepositoryImpl
import com.adel.data.repositories.UserRepositoryImpl
import com.adel.data.sources.roomDataSources.FcmRemoteDataSource
import com.adel.data.sources.roomDataSources.RoomRemoteDataSource
import com.adel.data.sources.userDataSources.UserRemoteDataSource
import com.adel.domain.repositories.RoomRepository
import com.adel.domain.repositories.UserRepository
import com.adel.domain.usecases.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase("admin")
    }
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson()
            }
        }
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<RoomRepository> {
        RoomRepositoryImpl(get(), get())
    }
    single {
        UserRemoteDataSource(get())
    }
    single {
        RegisterUseCase(get())
    }
    single {
        LoginUseCase(get())
    }
    single {
        UpdateUserDataUseCase(get())
    }
    single {
        GetUserFcmTokenUseCase(get())
    }
    single {
        GetUserProfileUseCase(get())
    }
    single {
        FcmRemoteDataSource(get())
    }
    single {
        RoomRemoteDataSource(get())
    }
    single {
        VerifyCodeUseCase(get())
    }
    single {
        SendEmailVerifyCodeUseCase(get())
    }
    single {
        DeleteAccountUseCase(get())
    }

    single {
        CreateRoomUseCase(get(), get(), get())
    }
    single {
        SendCallInvitationUseCase(get(), get())
    }

    single {
        DeleteRoomUseCase(get())
    }

    single {
        GetRoomInfoUseCase(get())
    }

    single {
        GetUserRoomsUseCase(get())
    }

    single {
        JoinRoomUseCase(get())
    }
}