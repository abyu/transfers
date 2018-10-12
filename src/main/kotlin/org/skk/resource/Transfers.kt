package org.skk.resource

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import org.skk.domain.Money
import org.skk.service.Transfer
import org.skk.service.TransferParams
import java.math.BigDecimal

data class TransferRequest(val sourceAccountId: String, val targetAccountId: String, val amount: BigDecimal)

class Transfers(private val transfer: Transfer) {

    suspend fun post(call: ApplicationCall) {
        val request = call.receive<TransferRequest>()

        val transferParams = request.let {
            TransferParams(senderAccountId = it.sourceAccountId.toLong(),
                    receiverAccountId = it.targetAccountId.toLong(),
                    amount = Money(it.amount))
        }

        val transactionStatus = transfer.forParams(transferParams)

        transactionStatus.isSuccess().takeIf { it }?.let {
            call.respond(HttpStatusCode.OK, TransferSuccessResponse())
        } ?: call.respond(HttpStatusCode.UnprocessableEntity, TransferFailureResponse(reason = transactionStatus.failureReason()))
    }
}

data class TransferSuccessResponse(val status: String = "Success")
data class TransferFailureResponse(val status: String = "Failed", val reason: String)