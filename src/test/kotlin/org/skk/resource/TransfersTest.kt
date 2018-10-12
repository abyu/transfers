package org.skk.resource

import assertk.all
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test
import org.skk.DependencyProvider
import org.skk.main
import org.skk.service.FailedTransaction
import org.skk.service.SuccessTransaction
import org.skk.service.Transfer
import org.skk.service.TransferParams
import java.math.BigDecimal

class TransfersTest {

    @Test
    fun `accept a transfer request with a json payload`() {
        mockkObject(DependencyProvider)
        val mockTransfer = mockk<Transfer>()
        every { DependencyProvider.getTransferService() } returns mockTransfer
        every { mockTransfer.forParams(TransferParams(1, 4, BigDecimal("100"))) } returns SuccessTransaction(BigDecimal("100"))

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"sourceAccountId": "1", "targetAccountId": "4", "amount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.contentType().match(ContentType.Application.Json)).isTrue()
                assert(actual.status()).isEqualTo(HttpStatusCode.OK)
                assert(actual.content).isEqualTo("""{"status":"Success"}""")
            }
        }
    }

    @Test
    fun `respond with 422 when transfer was a failure`() {
        mockkObject(DependencyProvider)
        val mockTransfer = mockk<Transfer>()
        every { DependencyProvider.getTransferService() } returns mockTransfer
        every { mockTransfer.forParams(TransferParams(1, 4, BigDecimal("100"))) } returns FailedTransaction("Insufficient funds")

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/transfer") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"sourceAccountId": "1", "targetAccountId": "4", "amount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.contentType().match(ContentType.Application.Json)).isTrue()
                assert(actual.status()).isEqualTo(HttpStatusCode.UnprocessableEntity)
                assert(actual.content).isEqualTo("""{"status":"Failed","reason":"Insufficient funds"}""")
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