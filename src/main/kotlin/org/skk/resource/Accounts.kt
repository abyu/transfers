package org.skk.resource

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import org.skk.domain.AccountEntity
import java.math.BigDecimal

class Accounts {

    suspend fun post(call: ApplicationCall) {
        val accountRequest = call.receive<AccountRequest>()

        val accountEntity = AccountEntity(name = accountRequest.name)
        accountEntity.save()

        call.respond(HttpStatusCode.Created)
    }
}

data class AccountRequest(val name: String, val initialAmount: BigDecimal)