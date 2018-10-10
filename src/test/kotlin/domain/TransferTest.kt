package org.skk.domain

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test
import java.math.BigDecimal

class TransferTest {

    @Test
    fun `transfer an amount from an account to another account`() {
        val account = Account("jake")
        account.credit(BigDecimal("1000"))
        val anotherAccount = Account("john")
        anotherAccount.credit(BigDecimal("200"))

        paymentTransaction {
            transfer amount BigDecimal("200")
            transfer from(account)
            transfer to(anotherAccount)
        }

        assert(account.getBalance()).isEqualTo(BigDecimal("800"))
        assert(anotherAccount.getBalance()).isEqualTo(BigDecimal("400"))
    }
}