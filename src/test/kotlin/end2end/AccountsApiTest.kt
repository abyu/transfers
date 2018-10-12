package end2end

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import io.ktor.http.HttpStatusCode
import khttp.post
import org.json.JSONObject
import org.junit.Ignore
import org.junit.Test

class AccountsApiTest {

    private val baseUrl = "http://localhost:8080"
    private val accountsUrl = "${baseUrl}/accounts"

    @Test
    fun `create a new account and retrieve by the id`() {
        val requestJson = JSONObject("""{"name": "Nemo", "initialAmount": "200"}""")

        val accountId = createAnAccount(accountsUrl, requestJson)

        val accountResponse = khttp.get("$accountsUrl/$accountId")
        val expectedJson = JSONObject("""{"id": $accountId, "name": "Nemo"}""")
        assert(accountResponse).all {
            assert(actual.statusCode).isEqualTo(HttpStatusCode.OK.value)
            assert(actual.jsonObject.toString()).isEqualTo(expectedJson.toString())
        }
    }

    @Test
    fun `create a new account and retrieve the available balance by the id`() {
        val requestJson = JSONObject("""{"name": "Nemo", "initialAmount": "200"}""")

        val accountId = createAnAccount(accountsUrl, requestJson)

        val accountResponse = khttp.get("$accountsUrl/$accountId/balance")
        val expectedJson = JSONObject("""{"accountId": $accountId, "balance": 200}""")

        assert(accountResponse).all {
            assert(actual.statusCode).isEqualTo(HttpStatusCode.OK.value)
            assert(actual.jsonObject.toString()).isEqualTo(expectedJson.toString())
        }
    }
}

fun createAnAccount(accountsUrl: String, requestJson: JSONObject): String? {
    val response = post(accountsUrl, json = requestJson)

    assert(response.statusCode).isEqualTo(HttpStatusCode.Created.value)

    return response.jsonObject.getString("accountId")
}