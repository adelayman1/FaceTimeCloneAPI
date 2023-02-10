package com.adel.data.utilities

import com.adel.data.utilities.UserJWTConfig.AUDIENCE
import com.adel.data.utilities.UserJWTConfig.ISSUER
import com.adel.data.utilities.UserJWTConfig.REFRESH_TOKEN_EXPIRE_DATE
import com.adel.data.utilities.UserJWTConfig.SECRET
import com.adel.data.models.TokenData
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object UserJWTConfig {
    val SECRET = System.getenv("roomsAPI_secret") ?: "secretKey"
    const val ISSUER = "http://127.0.0.1:8080/"
    const val AUDIENCE = "http://127.0.0.1:8080/user"
    const val REALM = "Access to rooms"
    const val REFRESH_TOKEN_EXPIRE_DATE = (12 * 30L * 24L * 60L * 60L * 1000L)
}
fun generateToken(tokenData: TokenData): String =
    JWT.create()
        .withAudience(AUDIENCE)
        .withIssuer(ISSUER)
        .withClaim("userId",tokenData.userId)
        .withClaim("verified",tokenData.verified)
        .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_DATE))
        .sign(Algorithm.HMAC256(SECRET))