package org.skk.resource

import assertk.all
import assertk.assertions.isEqualTo
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import org.skk.main

class AccountsTest{

    @Test
    fun `repond with the Created along with the account id when posted with account request`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/accounts") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"name": "Nemo", "initialAmount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.content).isEqualTo("""{"accountId": "2"}""")
                assert(actual.status()).isEqualTo(HttpStatusCode.Created)
            }
        }
    }

    @Test
    fun `respond with 400 bad request for missing json fields`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/accounts") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"initialAmount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }
}