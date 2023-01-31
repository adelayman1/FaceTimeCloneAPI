package com.adel.domain.models

data class ParticipantModel(
    var userEmail: String,
    var userName: String,
    var userId:String,
    var missedCall: Boolean = true,
)