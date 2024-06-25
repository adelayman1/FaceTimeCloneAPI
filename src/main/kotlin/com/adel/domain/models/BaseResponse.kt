package com.adel.domain.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.ktor.http.*

@JsonSerialize
sealed class BaseResponse<in T>(@JsonIgnore open val statuesCode: HttpStatusCode) {
    @JsonSerialize
    data class SuccessResponse<T>(
        val status: Boolean = true,
        val data: T? =null,
        val message: String = "",
        @JsonIgnore
        val statusCode: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(statusCode)

    @JsonSerialize
    data class ErrorResponse<T>(
        val status: Boolean = false,
        val data:T? = null,
        val message: String,
        @JsonIgnore
        val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<T>(statusCode)
}