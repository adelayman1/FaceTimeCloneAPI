package com.adel.routes.rooms

import com.adel.data.models.TokenData
import com.adel.domain.usecases.*
import com.adel.routes.rooms.requestModels.CreateRoomParams
import com.adel.routes.rooms.requestModels.JoinRoomParams
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

    authenticate("jwt_auth") {
        route(path = "/rooms") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val isAccountVerified = principal!!.payload.getClaim("verified").asBoolean()
                val userId = principal.payload.getClaim("userId").asString()
                val createRoomParams = call.receive<CreateRoomParams>()
                val createRoomResult = createRoomUseCase(TokenData(userId = userId, verified = isAccountVerified),createRoomParams)
                call.respond(message = createRoomResult, status = createRoomResult.statuesCode)
            }
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val getUserRoomsResult = getUserRoomsUseCase(TokenData(userId = userId, verified = isAccountVerified),)
                call.respond(message = getUserRoomsResult, status = getUserRoomsResult.statuesCode)
            }
            get("/{room_id}"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val getRoomInfoResult = getRoomInfoUseCase(TokenData(userId = userId, verified = isAccountVerified), roomId = call.parameters["room_id"])
                call.respond(message = getRoomInfoResult, status = getRoomInfoResult.statuesCode)
            }
            delete("/{room_id}")  {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val deleteRoomResult = deleteRoomUseCase(TokenData(userId = userId, verified = isAccountVerified), roomId = call.parameters["room_id"])
                call.respond(message = deleteRoomResult, status = deleteRoomResult.statuesCode)
            }
            post("/{room_id}/join"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                val joinRoomParams = call.receive<JoinRoomParams>()
                val joinRoomResult = joinRoomUseCase(TokenData(userId = userId, verified = isAccountVerified), roomId = joinRoomParams.roomId)
                call.respond(message = joinRoomResult, status = joinRoomResult.statuesCode)
            }
        }
    }
}