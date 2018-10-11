package org.skk

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import org.skk.resource.Accounts
import org.skk.resource.Health
import org.skk.resource.Transfer

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
        val databaseConfig = config.databaseConfig
    }
}

fun Application.main() {
    val health = DependencyProvider.healthResource()
    val transfer = DependencyProvider.transferResource()
    val account = DependencyProvider.accountsResource()

    install(ContentNegotiation) {
        jackson {
        }
    }

    install(StatusPages) {
        exception<JsonParseException> {
            call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
        }

        exception<InvalidFormatException> {
            call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
        }

        exception<MissingKotlinParameterException> {
            call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
        }
    }

    routing {
        get("/health") {
            health.get(call)
        }
        post("/transfer") {
            transfer.post(call)
        }
        post("/accounts") {
            account.post(call)
        }
    }
}