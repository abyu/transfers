package org.skk.service

import org.skk.domain.Money
import org.skk.domain.Transaction

class DebitTransaction(override val accountId: Long, val transactionAmount: Money) : TransactionOperation {
    override fun execute(vaultAmount: Money): TransactionStatus {

        val result = transactionAmount.takeIf { it isLessThan vaultAmount}?.let {
            SuccessTransaction(resultingAmount = (vaultAmount minus it).value)
        } ?: FailedTransaction(reason = "Insufficient funds")

        val transaction = Transaction(transactionType = "DEBIT",
                accountId = accountId,
                amount = transactionAmount.value,
                status = result.status())
        transaction.save()

        return result
    }
}
