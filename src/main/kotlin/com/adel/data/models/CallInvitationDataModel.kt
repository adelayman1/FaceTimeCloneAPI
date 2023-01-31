package com.adel.data.models

// type is invitation type ["call","response"]
data class CallInvitationDataModel(var name: String, var type: String,var response: String,var key:String,var authorUID:String)