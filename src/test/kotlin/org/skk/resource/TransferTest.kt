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

class TransferTest {

    @Test
    fun `accept a transfer request with a json payload`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"sourceAccountId": "1", "targetAccountId": "4", "amount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.OK)
            }
        }
    }

    @Test
    fun `respond with 400 bad request for an invalid json request`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"an invalid json"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    @Test
    fun `respond with 400 bad request when the json request has non numeric amount`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"sourceAccountId": "1", "targetAccountId": "4", "amount": "not a number"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }
}