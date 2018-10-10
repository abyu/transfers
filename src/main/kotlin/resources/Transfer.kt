package org.skk.resources

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import java.math.BigDecimal

data class TransferRequest(val sourceAccountId: String, val targetAccountId : String, val amount : BigDecimal)

class Transfer {

    suspend fun post(call: ApplicationCall) {
        val request = call.receive<TransferRequest>()

        call.respond(HttpStatusCode.OK)
    }
}