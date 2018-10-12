package end2end

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import io.ktor.http.HttpStatusCode
import khttp.get
import org.json.JSONObject
import org.junit.Ignore
import org.junit.Test
import java.math.BigDecimal

@Ignore("Figure out a way to run this from gradle")
class TransfersApiTest {

    private val baseUrl = "http://localhost:8080"

    @Test
    fun `transfer money from one account to another account`(){
        val senderAccountRequest = JSONObject("""{"name": "Sendie", "initialAmount": "2300"}""")
        val receiverAccountRequest = JSONObject("""{"name": "Receivo", "initialAmount": "700"}""")
        val senderAccountId = createAnAccount("$baseUrl/accounts", senderAccountRequest)
        val receiverAccountId = createAnAccount("$baseUrl/accounts", receiverAccountRequest)
        val transferAmount = 300
        val transferRequest = JSONObject("""{"sourceAccountId":"$senderAccountId", "targetAccountId":"$receiverAccountId", "amount": "$transferAmount" }""")

        val transferResponse = khttp.post("$baseUrl/transfers", json = transferRequest)

        assert(transferResponse).all{
            assert(actual.statusCode).isEqualTo(HttpStatusCode.OK.value)
            assert(actual.jsonObject.getString("status")).isEqualTo("Success")
        }

        val senderBalance = geBalance(senderAccountId)
        val receiverBalance = geBalance(receiverAccountId)

        assert(senderBalance).isEqualTo(BigDecimal("2000"))
        assert(receiverBalance).isEqualTo(BigDecimal("1000"))
    }

    @Test
    fun `failure transfer from one account to another account when sender does not have sufficient funds`(){
        val senderAccountRequest = JSONObject("""{"name": "Sendie", "initialAmount": "280"}""")
        val receiverAccountRequest = JSONObject("""{"name": "Receivo", "initialAmount": "700"}""")
        val senderAccountId = createAnAccount("$baseUrl/accounts", senderAccountRequest)
        val receiverAccountId = createAnAccount("$baseUrl/accounts", receiverAccountRequest)

        val transferAmount = 300
        val transferRequest = JSONObject("""{"sourceAccountId":"$senderAccountId", "targetAccountId":"$receiverAccountId", "amount": "$transferAmount" }""")
        val transferResponse = khttp.post("$baseUrl/transfers", json = transferRequest)

        val expectedResponse = JSONObject("""{"status":"Failed", "reason": "Insufficient funds"}""")
        assert(transferResponse).all{
            assert(actual.statusCode).isEqualTo(HttpStatusCode.UnprocessableEntity.value)
            assert(actual.jsonObject.toString()).isEqualTo(expectedResponse.toString())
        }

        val senderBalance = geBalance(senderAccountId)
        val receiverBalance = geBalance(receiverAccountId)

        assert(senderBalance).isEqualTo(BigDecimal("280"))
        assert(receiverBalance).isEqualTo(BigDecimal("700"))
    }

    private fun geBalance(accountId: String?): BigDecimal {
        val getBalanceResponse = get("$baseUrl/accounts/$accountId/balance")

        assert(getBalanceResponse.statusCode).isEqualTo(HttpStatusCode.OK.value)

        return BigDecimal(getBalanceResponse.jsonObject.getBigInteger("balance"))
    }
}