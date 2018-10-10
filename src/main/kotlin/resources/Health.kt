package org.skk.resources

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respondText

class Health{

    suspend fun get(call: ApplicationCall) {
        call.respondText("""{"status": "UP"}""", ContentType.Application.Json)
    }
}