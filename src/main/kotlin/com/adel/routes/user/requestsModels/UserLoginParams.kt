package com.adel.routes.user.requestsModels

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserLoginParams(var email: String, var password: String)