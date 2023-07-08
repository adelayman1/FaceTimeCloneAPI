package com.adel.routes.user

import com.adel.data.models.TokenData
import com.adel.data.models.User
import com.adel.data.utilities.Constants
import com.adel.domain.usecases.*
import com.adel.routes.user.requestsModels.*
import com.mongodb.ConnectionString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun Route.userRoute() {
    val registerUseCase by inject<RegisterUseCase>()
    val loginUseCase by inject<LoginUseCase>()
    val updateUserDataUseCase by inject<UpdateUserDataUseCase>()
    val getUserFcmTokenUseCase by inject<GetUserFcmTokenUseCase>()
    val getUserProfileUseCase by inject<GetUserProfileUseCase>()
    val sendEmailVerifyCodeUseCase by inject<SendEmailVerifyCodeUseCase>()
    val verifyCodeUseCase by inject<VerifyCodeUseCase>()
    val deleteAccountUseCase by inject<DeleteAccountUseCase>()
    route(path = "/user") {
        post(path = "/register") {
            val userFormParameters = call.receive<CreateUserParams>()
            val registerResult = registerUseCase(
                name = userFormParameters.name,
                email = userFormParameters.email,
                password = userFormParameters.password,
                fcmToken = userFormParameters.fcmToken
            )
            call.respond(message = registerResult, status = registerResult.statuesCode)
        }
        post(path = "/login") {
            try {
                val userFormParameters = call.receive<UserLoginParams>()
                val loginResult = loginUseCase(email = userFormParameters.email, password = userFormParameters.password)
                call.respond(message = loginResult, status = loginResult.statuesCode)
            }catch (e:Exception){
                call.respond(message = e.message.toString(), status = HttpStatusCode.BadRequest)
            }

        }
        authenticate("jwt_auth") {
            route("fcm-token") {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asString()
                    val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                    val getUserFcmTokenResult = getUserFcmTokenUseCase(TokenData(userId = userId, verified = isAccountVerified))
                    call.respond(message = getUserFcmTokenResult, status = getUserFcmTokenResult.statuesCode)
                }
                patch {

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asString()
                    val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                    val updateUserBodyParameters = call.receive<UpdateUserTokenParams>()
                    val updateUserDataResult = updateUserDataUseCase(
                        TokenData(userId = userId, verified = isAccountVerified),
                        userFcmToken = updateUserBodyParameters.fcmToken
                    )
                    call.respond(message = updateUserDataResult, status = updateUserDataResult.statuesCode)
                }
            }
            patch {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asString()
                    val isAccountVerified = principal.payload.getClaim("verified").asBoolean()
                    val updateUserBodyParameters = call.receive<UpdateUserParams>()
                    val updateUserDataResult = updateUserDataUseCase(
                        TokenData(userId = userId, verified = isAccountVerified),
                        name = updateUserBodyParameters.name
                    )
                    call.respond(message = updateUserDataResult, status = updateUserDataResult.statuesCode)
                } catch (e: Exception) {
                    println("error" + e.message.toString())
                }
            }
            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val getProfileBodyParameters = call.receive<GetProfileParams>();
                val getUserProfileResult = getUserProfileUseCase(userId = getProfileBodyParameters.userId)
                call.respond(message = getUserProfileResult, status = getUserProfileResult.statuesCode)
            }
            post("/send-email-code") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val sendEmailVerifyCodeResult = sendEmailVerifyCodeUseCase(userId = userId)
                call.respond(message = sendEmailVerifyCodeResult, status = sendEmailVerifyCodeResult.statuesCode)
            }
            post("/verify-code") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val otp = call.request.queryParameters["otp_code"]?.toIntOrNull()
                val verifyCodeResult = verifyCodeUseCase(userId = userId, otp)
                call.respond(message = verifyCodeResult, status = verifyCodeResult.statuesCode)
            }
            delete {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asString()
                val deleteAccountResult = deleteAccountUseCase(userId = userId)
                call.respond(message = deleteAccountResult, status = deleteAccountResult.statuesCode)
            }
        }
    }
}