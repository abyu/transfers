package org.skk.domain

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.ebean.EbeanServer
import io.ebean.MockiEbean
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Test
import org.skk.service.CreditTransaction
import org.skk.service.TransactionStatus
import java.math.BigDecimal

class CreditTransactionTest {

    @Test
    fun `credit adds an amount to given vault amount`() {
        val creditTransaction = CreditTransaction(2, BigDecimal("100"))

        val status: TransactionStatus = creditTransaction.execute(BigDecimal("100"))

        assert(status.isSuccess()).isTrue()
        assert(status.resultAmount()).isEqualTo(BigDecimal("200"))
    }

    @Test
    fun `credit transaction persists the transaction to database on execution`() {
        val mockEbeanServer = mockk<EbeanServer>()
        val slot = slot<Transaction>()
        every { mockEbeanServer.save(capture(slot)) } just runs

        MockiEbean.runWithMock(mockEbeanServer) {
            val creditTransaction = CreditTransaction(2, BigDecimal("100"))

            val status: TransactionStatus = creditTransaction.execute(BigDecimal("100"))

            assert(slot.isCaptured).isTrue()
            assert(slot.captured).all {
                assert(actual.transactionType).isEqualTo("CREDIT")
                assert(actual.accountId).isEqualTo(2L)
                assert(actual.amount).isEqualTo(BigDecimal("100"))
                assert(actual.status).isEqualTo(status.status())
            }
        }

    }

}