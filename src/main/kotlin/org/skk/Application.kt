package org.skk

import io.ebean.Ebean
import io.ebean.EbeanServerFactory
import io.ebean.config.ServerConfig
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

class Application(private val config: Config) {

    fun init() {
        setUpDataBaseInContext()
        val server = setUpServer()

        server.start(wait = true)
    }

    private fun setUpServer(): NettyApplicationEngine {

        return embeddedServer(Netty, module = Application::main, port = config.appPort)
    }

    private fun setUpDataBaseInContext() {
        val serverConfig = ServerConfig()
        serverConfig.loadFromProperties()
        serverConfig.isDefaultServer = true

        EbeanServerFactory.create(serverConfig)

        Ebean.getDefaultServer()
    }
}

fun Application.main() {

    install(ContentNegotiation) {
        jackson {
        }
    }

    install(StatusPages) {
        exceptionResponseHandlers()
    }

    routing {
        routeConfiguration(DependencyProvider)
    }
}
