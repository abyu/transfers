package org.skk.service

import org.skk.domain.Transaction
import java.math.BigDecimal

class DebitTransaction(override val accountId: Long, val transactionAmount: BigDecimal) : TransactionOperation {
    override fun execute(vaultAmount: BigDecimal): TransactionStatus {

        val result = transactionAmount.takeIf { it <= vaultAmount }?.let {
            SuccessTransaction(resultingAmount = vaultAmount - it)
        } ?: FailedTransaction(reason = "Insufficient funds")

        val transaction = Transaction(transactionType = "DEBIT", accountId = accountId, amount = transactionAmount, status = result.status())
        transaction.save()

        return result
    }
}
