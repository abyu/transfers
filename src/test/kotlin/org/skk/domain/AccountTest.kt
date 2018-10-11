package org.skk.domain

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test
import org.skk.database.AccountEntity
import org.skk.database.WalletEntity
import java.math.BigDecimal

class AccountTest {

    @Test
    fun `credit adds the given amount to the balance`() {
        val account = Account(id = 1, name="some")

        account.save()

    }
//
//    @Test
//    fun `debit removes the given amount to the balance`() {
//        val account = Account(AccountEntity(id = 2, wallet = WalletEntity(1, BigDecimal("100"))))
//
//        account.debit(BigDecimal("50"))
//
//        assert(account.getBalance()).isEqualTo(BigDecimal("50"))
//    }
//
//    @Test
//    fun `debit fails when the requested amount is less than the available balance`() {
//        val account = Account(AccountEntity(id = 2, wallet = WalletEntity(1, BigDecimal("100"))))
//
//        val status = account.debit(BigDecimal("200"))
//
//        assert(status.status).isEqualTo("FAILED")
//        assert(status.reason).isEqualTo("Insufficient funds")
//    }


}
