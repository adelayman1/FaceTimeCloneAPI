package com.adel.domain.models

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class RoomType(val id:Int,val type:String){
    LINK(1,"Link"),
    FACETIME(2,"FaceTime"),
    AUDIO(3,"AudioCall");
    companion object {
        fun valueOf(value: Int) = values().find { it.id == value }
    }
}