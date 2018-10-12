package org.skk.service

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
import org.skk.domain.Transaction
import org.skk.domain.pounds
import java.math.BigDecimal

class DebitTransactionTest {

    @Test
    fun `debit transaction debits the amount from the given vault amount`() {
        val debitTransaction = DebitTransaction(accountId = 1, transactionAmount = "100".pounds())

        val status = debitTransaction.execute("200".pounds())

        assert(status.isSuccess()).isTrue()
        assert(status.resultAmount()).isEqualTo(BigDecimal("100"))
    }

    @Test
    fun `debit transaction returns failure result when the amount is more than the given vault amount`() {
        val debitTransaction = DebitTransaction(accountId = 1, transactionAmount = "400".pounds())

        val originalVaultAmount = "200".pounds()
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
            val debitTransaction = DebitTransaction(accountId = 1, transactionAmount = "100".pounds())

            val status = debitTransaction.execute("200".pounds())

            assert(slot.isCaptured).isTrue()
            assert(slot.captured).all {
                assert(actual.accountId).isEqualTo(1L)
                assert(actual.status).isEqualTo(status.status())
                assert(actual.amount).isEqualTo(BigDecimal("100"))
                assert(actual.transactionType).isEqualTo("DEBIT")
            }
        }
    }
}

