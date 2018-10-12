package org.skk.service

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
import org.skk.domain.Transaction
import org.skk.domain.pounds
import java.math.BigDecimal

class CreditTransactionTest {

    @Test
    fun `credit adds an amount to given vault money`() {
        val creditTransaction = CreditTransaction(2, "100".pounds())

        val status: TransactionStatus = creditTransaction.execute("100".pounds())

        assert(status.isSuccess()).isTrue()
        assert(status.resultAmount()).isEqualTo(BigDecimal("200"))
    }

    @Test
    fun `credit transaction persists the transaction to database on execution`() {
        val mockEbeanServer = mockk<EbeanServer>()
        val slot = slot<Transaction>()
        every { mockEbeanServer.save(capture(slot)) } just runs

        MockiEbean.runWithMock(mockEbeanServer) {
            val creditTransaction = CreditTransaction(2, "100".pounds())

            val status: TransactionStatus = creditTransaction.execute("100".pounds())

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