package com.adel.routes.rooms

import com.adel.domain.usecases.*
import com.adel.routes.rooms.requestModels.CreateRoomParams
import com.adel.routes.rooms.requestModels.JoinRoomParams
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.roomsRoute() {
    val createRoomUseCase by inject<CreateRoomUseCase>()
    val deleteRoomUseCase by inject<DeleteRoomUseCase>()
    val getRoomInfoUseCase by inject<GetRoomInfoUseCase>()
    val getUserRoomsUseCase by inject<GetUserRoomsUseCase>()
    val joinRoomUseCase by inject<JoinRoomUseCase>()

    val client = HttpClient(CIO){
        install(ContentNegotiation){
            jackson()
        }
    }
    authenticate("jwt_auth") {
        route(path = "/rooms") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val isAccountVerified = principal!!.payload.getClaim("verified").asBoolean()
                val userId = principal.payload.getClaim("userId").asString()
                val createRoomParams = call.receive<CreateRoomParams>()
                val createRoomResult = createRoomUseCase(client, userId, verified = isAccountVerified,createRoomParams.roomType, createRoomParams.participantsEmails)
                call.respond(message = createRoomResult, status = createRoomResult.statuesCode)
            }
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val getUserRoomsResult = getUserRoomsUseCase(userId, verified = isAccountVerified)
                call.respond(message = getUserRoomsResult, status = getUserRoomsResult.statuesCode)
            }
            get("/{room_id}"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val getRoomInfoResult = getRoomInfoUseCase(userId = userId, roomId = call.parameters["room_id"], verified = isAccountVerified)
                call.respond(message = getRoomInfoResult, status = getRoomInfoResult.statuesCode)
            }
            delete("/{room_id}")  {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val deleteRoomResult = deleteRoomUseCase(userId = userId, roomId = call.parameters["room_id"], verified = isAccountVerified)
                call.respond(message = deleteRoomResult, status = deleteRoomResult.statuesCode)
            }
            post("/{room_id}/join"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val joinRoomParams = call.receive<JoinRoomParams>()
                val joinRoomResult = joinRoomUseCase(userId = userId, roomId = joinRoomParams.roomId, verified = isAccountVerified)
                call.respond(message = joinRoomResult, status = joinRoomResult.statuesCode)
            }
        }
    }
}