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
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test
import org.skk.DependencyProvider
import org.skk.main
import org.skk.service.Account
import org.skk.service.AccountCreationException
import java.lang.Exception
import java.math.BigDecimal

class AccountsTest{

    @Test
    fun `respond with the Created along with the account id when posted with account request`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.createNew("Nemo", BigDecimal("100")) } returns 2

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
    fun `respond with 500 server error when account creation fails`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.createNew("Nemo", BigDecimal("100")) } throws AccountCreationException("Creation failed", Exception())

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Post, "/accounts") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"name": "Nemo", "initialAmount": "100"}""")
            }.response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.InternalServerError)
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