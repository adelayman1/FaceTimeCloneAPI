package com.adel.plugins

import com.adel.data.utilities.UserJWTConfig.AUDIENCE
import com.adel.data.utilities.UserJWTConfig.ISSUER
import com.adel.data.utilities.UserJWTConfig.REALM
import com.adel.data.utilities.UserJWTConfig.SECRET
import com.adel.domain.models.BaseResponse
import com.adel.domain.models.RoomModel
import com.adel.domain.repositories.UserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val usersRepo by inject<UserRepository>()
    authentication {
        jwt(name = "jwt_auth") {
            realm = REALM
            verifier(
                JWT
                    .require(Algorithm.HMAC256(SECRET))
                    .withAudience(AUDIENCE)
                    .withIssuer(ISSUER)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                val isUserExist = usersRepo.getUserById(userID = userId) != null
                if (isUserExist) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, BaseResponse.ErrorResponse<RoomModel>(message = "Token is not valid"))
            }
        }
    }

}
