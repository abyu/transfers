package org.skk.integrationTests

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Ignore
import org.junit.Test

@Ignore("Figure out a way to run this from gradle")
class HealthApiTest {

    @Test
    fun `health api returns a success with a status up`(){
        val response = khttp.get("http://localhost:8080/health")

        assert(response.statusCode).isEqualTo(200)
        assert(response.text).isEqualTo("""{"status": "UP"}""")
    }
}