package com.adel.plugins

import com.adel.routes.rooms.roomsRoute
import com.adel.routes.user.userRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoute()
        roomsRoute()
    }
}
