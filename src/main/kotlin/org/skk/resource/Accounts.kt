package org.skk.resource

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import org.skk.service.Account
import java.math.BigDecimal

class Accounts(private val account: Account) {

    suspend fun post(call: ApplicationCall) {
        val accountRequest = call.receive<AccountRequest>()

        val newAccountId = account.createNew(accountRequest.name, accountRequest.initialAmount)

        call.respond(HttpStatusCode.Created, CreateAccountResponse(newAccountId.toString()))
    }

    suspend fun get(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLong()
        id?.let {
            call.respond(HttpStatusCode.OK, account.getAccount(it))
        }
    }
}

data class AccountRequest(val name: String, val initialAmount: BigDecimal)
data class CreateAccountResponse(val accountId: String)