package org.skk.resources

import assertk.all
import assertk.assertions.isEqualTo
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import org.skk.main

class HealthTest {

    @Test
    fun `test health endpoint`() {
        withTestApplication(Application::main) {
            val response = handleRequest(HttpMethod.Get, "/health").response
            assertk.assert(response).all {
                assert(actual.status()).isEqualTo(HttpStatusCode.OK)
                assert(actual.content).isEqualTo("""{"status": "UP"}""")
            }
        }
    }
}