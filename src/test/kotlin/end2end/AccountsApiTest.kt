package end2end

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import io.ktor.http.HttpStatusCode
import khttp.post
import org.json.JSONObject
import org.junit.Ignore
import org.junit.Test

@Ignore("Figure out a way to run this from gradle, startup server manually before running")
class AccountsApiTest {

    private val baseUrl = "http://localhost:8080"
    private val accountsUrl = "${baseUrl}/accounts"

    @Test
    fun `create a new account and retrieve by the id`() {
        val requestJson = JSONObject("""{"name": "Nemo", "initialAmount": "200"}""")

        val accountId = createAnAccount(requestJson)

        val accountResponse = khttp.get("$accountsUrl/$accountId")

        assert(accountResponse).all {
            assert(actual.statusCode).isEqualTo(HttpStatusCode.OK.value)
        }
    }

    private fun createAnAccount(requestJson: JSONObject): String? {
        val response = post(accountsUrl, json = requestJson)

        assert(response.statusCode).isEqualTo(HttpStatusCode.Created.value)

        return response.jsonObject.getString("accountId")
    }
}