package org.skk.resource

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import org.skk.domain.Money
import org.skk.service.TransactionStatus
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

        call.respond(transactionStatus.responseCode(), transactionStatus.responseContext())
    }

    private fun TransactionStatus.responseCode() : HttpStatusCode {
        return when {
            isSuccess() -> HttpStatusCode.OK
            else -> HttpStatusCode.UnprocessableEntity
        }
    }

    private fun TransactionStatus.responseContext() : TransferResponse {
        return when {
            isSuccess() -> TransferSuccessResponse()
            else -> TransferFailureResponse(reason = failureReason())
        }
    }
}

interface TransferResponse
data class TransferSuccessResponse(val status: String = "Success"): TransferResponse
data class TransferFailureResponse(val status: String = "Failed", val reason: String): TransferResponse