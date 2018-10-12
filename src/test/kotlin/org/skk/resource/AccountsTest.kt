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
import org.skk.model.AccountBalance
import org.skk.service.Account
import org.skk.service.AccountCreationException
import org.skk.service.AccountNotFoundException
import org.skk.service.VaultNotFoundException
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
                assert(actual.contentType().match(ContentType.Application.Json.toString())).isTrue()
                assert(actual.content).isEqualTo("""{"accountId":"2"}""")
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
    fun `get the account with given Id`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.getAccount(2L) } returns org.skk.model.Account(2, "Nemo")

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Get, "/accounts/2").response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.OK)
                assert(actual.content).isEqualTo("""{"id":2,"name":"Nemo"}""")
            }
        }
    }

    @Test
    fun `retrieve the balance of an existing account`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.getBalance(2L) } returns AccountBalance(2, BigDecimal("200"))

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Get, "/accounts/2/balance").response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.OK)
                assert(actual.content).isEqualTo("""{"accountId":2,"balance":200}""")
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

    @Test
    fun `respond with 404 when an account with given Id does not exist`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.getAccount(2L) } throws AccountNotFoundException(2L)

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Get, "/accounts/2").response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.NotFound)
            }
        }
    }

    @Test
    fun `get balance responds with 409 conflice when the vault does not exist yet for an account`() {
        mockkObject(DependencyProvider)
        val mockAccount = mockk<Account>()
        every { DependencyProvider.getAccountService() } returns mockAccount
        every { mockAccount.getBalance(2L) } throws VaultNotFoundException(2L)

        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Get, "/accounts/2/balance").response

            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.Conflict)
            }
        }
    }
}