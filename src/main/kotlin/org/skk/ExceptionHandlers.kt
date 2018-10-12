package org.skk

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.skk.exceptions.AccountCreationException
import org.skk.exceptions.AccountNotFoundException
import org.skk.exceptions.VaultNotFoundException

fun StatusPages.Configuration.exceptionResponseHandlers() {
    exception<JsonParseException> {
        call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
    }

    exception<InvalidFormatException> {
        call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
    }

    exception<MissingKotlinParameterException> {
        call.respond(HttpStatusCode.BadRequest, "The request cannot be parsed to a valid json")
    }

    exception<AccountCreationException> {
        call.respond(HttpStatusCode.InternalServerError, "Failed while trying to create an account, error: ${it.message} ")
    }

    exception<AccountNotFoundException> {
        call.respond(HttpStatusCode.NotFound, "${it.message}")
    }

    exception<VaultNotFoundException> {
        call.respond(HttpStatusCode.Conflict, "${it.message}")
    }
}