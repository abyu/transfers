package org.skk

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
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
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.skk.database.getAllTables
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
        Database.connect(databaseConfig.connectionString, driver = databaseConfig.driver)

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(*getAllTables())
        }
    }
}

fun Application.main() {
    val health = Health()
    val transfer = Transfer()

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
    }

    routing {
        get("/health") {
            health.get(call)
        }
        post("/transfer") {
            transfer.post(call)
        }
    }
}