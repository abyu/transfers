package org.skk.domain

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import org.junit.Test
import org.skk.exceptions.IllegalMoneyOperationException

class MoneyTest {

    @Test
    fun `100 pounds is same as another 100 pounds`() {
        val hundredPounds = "100".pounds()
        val anotherHundredPounds = "100".pounds()

        assert(hundredPounds).isEqualTo(anotherHundredPounds)
    }

    @Test
    fun `2 pounds is same as 5 pounds`() {
        val twoPounds = "2".pounds()
        val fivePounds = "5".pounds()

        assert(twoPounds).isNotEqualTo(fivePounds)
    }

    @Test
    fun `100 pounds plus 50 pounds is 150 pounds`() {
        val hundredPounds = "100".pounds()
        val fiftyPounds = "50".pounds()

        val newValue = hundredPounds plus fiftyPounds

        assert(newValue).isEqualTo("150".pounds())
    }

    @Test
    fun `80 pounds minus 50 pounds is 30 pounds`() {
        val eightyPounds = "80".pounds()
        val fiftyPounds = "50".pounds()

        val newValue = eightyPounds minus fiftyPounds

        assert(newValue).isEqualTo("30".pounds())
    }

    @Test
    fun `50 pounds minus 100 pounds is illegal`() {
        val fiftyPounds = "50".pounds()
        val hundredPounds = "80".pounds()

        assert {
            fiftyPounds minus hundredPounds
        }.thrownError { isInstanceOf(IllegalMoneyOperationException::class) }
    }
}