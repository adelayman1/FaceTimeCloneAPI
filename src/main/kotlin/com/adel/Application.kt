package com.adel

import com.adel.di.modules.mainModule
import com.adel.domain.usecases.LoginUseCase
import com.adel.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }

    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
