package org.skk.domain

import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Test
import org.skk.database.AccountEntity
import org.skk.database.WalletEntity
import java.math.BigDecimal

class TransferTest {

//    @Test
//    fun `transfer an amount from an account to another account`() {
//        val account = Account(AccountEntity(id = 1, wallet = WalletEntity(1, BigDecimal("1000"))))
//        val anotherAccount = Account(AccountEntity(id = 2, wallet = WalletEntity(1, BigDecimal("200"))))
//
//        paymentTransaction {
//            transfer amount BigDecimal("200")
//            transfer from(account)
//            transfer to(anotherAccount)
//        }
//
//        assert(account.getBalance()).isEqualTo(BigDecimal("800"))
//        assert(anotherAccount.getBalance()).isEqualTo(BigDecimal("400"))
//    }
//
//    @Test
//    fun `transfer fails when the source account does not have sufficient funds`() {
//        val account = Account(AccountEntity(id = 1, wallet = WalletEntity(1, BigDecimal("1000"))))
//        val anotherAccount = Account(AccountEntity(id = 1, wallet = WalletEntity(1, BigDecimal("500"))))
//
//        val status : TransactionStatus = paymentTransaction {
//            transfer amount BigDecimal("2000")
//            transfer from(account)
//            transfer to(anotherAccount)
//        }
//
//        assert(status.status).isEqualTo("FAILED")
//        assert(status.reason).isEqualTo("Insufficient funds")
//        assert(account.getBalance()).isEqualTo(BigDecimal("1000"))
//        assert(anotherAccount.getBalance()).isEqualTo(BigDecimal("500"))
//    }
}