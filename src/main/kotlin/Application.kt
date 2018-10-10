package org.skk

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
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

class Application(private val config: Config) {

    fun init() {
        setUpDataBaseInContext()
        val server = setUpServer()

        server.start(wait = true)
    }

    private fun setUpServer(): NettyApplicationEngine {

        return embeddedServer(Netty, port = config.appPort) {
            routing {
                get("/health") {
                    call.respondText("""{"status": "UP"}""", ContentType.Application.Json)
                }
            }
        }
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