package org.skk.service

import assertk.all
import assertk.assertions.isTrue
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import java.math.BigDecimal

class TransferTest {

    @Test
    fun `executes a debit transaction on vault for the source account returning the result`() {
        val vault = mockk<Vault>(relaxed = true)
        val transfer = Transfer(vault)
        val senderAccountId = 1L
        val receiverAccountId = 2L
        val transferAmount = BigDecimal("100")
        every { vault.execute(debitTransactionMatching(senderAccountId)) } returns SuccessTransaction(transferAmount)

        val status: TransactionStatus = transfer.forParams(
            TransferParams(
                    senderAccountId = senderAccountId,
                    receiverAccountId = receiverAccountId,
                    amount = transferAmount)
                )

        assert(status.isSuccess()).isTrue()
        verify { vault.execute(debitTransactionMatching(senderAccountId = senderAccountId)) }
    }

    @Test
    fun `executes a credit transaction on vault for the receiving account when source account debit was successful`() {
        val vault = mockk<Vault>()
        val transfer = Transfer(vault)
        val senderAccountId = 1L
        val receiverAccountId = 2L
        val transferAmount = BigDecimal("100")
        every { vault.execute(debitTransactionMatching(senderAccountId)) } returns SuccessTransaction(transferAmount)
        every { vault.execute(creditTransactionMatching(receiverAccountId)) } returns SuccessTransaction(transferAmount)

        val status: TransactionStatus = transfer.forParams(
            TransferParams(
                    senderAccountId = senderAccountId,
                    receiverAccountId = receiverAccountId,
                    amount = transferAmount)
                )

        assert(status.isSuccess()).isTrue()
        verify {
            vault.execute(debitTransactionMatching(senderAccountId))
            vault.execute(creditTransactionMatching(receiverAccountId))
        }
    }

    @Test
    fun `aborts a transfer when the source account debit returns a failure`() {
        val vault = mockk<Vault>()
        val transfer = Transfer(vault)
        val senderAccountId = 1L
        val receiverAccountId = 2L
        val transferAmount = BigDecimal("100")
        every { vault.execute(debitTransactionMatching(senderAccountId)) } returns FailedTransaction("Insufficient funds")

        val status: TransactionStatus = transfer.forParams(
            TransferParams(
                    senderAccountId = senderAccountId,
                    receiverAccountId = receiverAccountId,
                    amount = transferAmount)
                )

        assert(status.isSuccess()).isFalse()
        verify {
            vault.execute(debitTransactionMatching(senderAccountId))
        }
        verify(exactly = 0) {
            vault.execute(creditTransactionMatching(receiverAccountId))
        }
    }

    private fun MockKMatcherScope.creditTransactionMatching(receiverAccountId: Long): TransactionOperation =
            match { t -> t is CreditTransaction && t.accountId == receiverAccountId }

    private fun MockKMatcherScope.debitTransactionMatching(senderAccountId: Long): TransactionOperation =
            match { t -> t is DebitTransaction && t.accountId == senderAccountId }

}