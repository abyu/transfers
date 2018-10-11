package org.skk.domain

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.ebean.EbeanServer
import io.ebean.MockiEbean
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import java.math.BigDecimal

class TransactionTest {

    @Test
    fun `debit transaction debits the amount from the given vault amount`() {
        val debitTransaction = DebitTransaction(accountId=1, transactionAmount=BigDecimal("100"))

        val status = debitTransaction.execute(BigDecimal("200"))

        assert(status.isSuccess()).isTrue()
        assert(status.resultAmount()).isEqualTo(BigDecimal("100"))
    }

    @Test
    fun `debit transaction returns failure result when the amount is more than the given vault amount`() {
        val debitTransaction = DebitTransaction(accountId=1, transactionAmount=BigDecimal("400"))

        val originalVaultAmount = BigDecimal("200")
        val status: TransactionStatus = debitTransaction.execute(originalVaultAmount)

        assert(status.isSuccess()).isFalse()
        assert(status.failureReason()).isEqualTo("Insufficient funds")
    }

    @Test
    fun `debit transaction persists the transaction to the transactions table on execution`() {
        val mockEbeanServer = mockk<EbeanServer>()
        val slot = slot<Transaction>()
        every { mockEbeanServer.save(capture(slot)) } just runs

        MockiEbean.runWithMock(mockEbeanServer) {
            val debitTransaction = DebitTransaction(accountId=1, transactionAmount=BigDecimal("100"))

            val status = debitTransaction.execute(BigDecimal("200"))

            assert(slot.isCaptured).isTrue()
            assert(slot.captured).all {
                assert(actual.accountId).isEqualTo(1)
                assert(actual.status).isEqualTo("SUCCESS")
                assert(actual.amount).isEqualTo(BigDecimal("100"))
                assert(actual.transactionType).isEqualTo(status.status())
            }
        }
    }
}