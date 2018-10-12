package org.skk.service

import org.skk.domain.Transaction
import java.math.BigDecimal

class CreditTransaction(override val accountId: Long, private val transactionAmount: BigDecimal) : TransactionOperation {
    override fun execute(vaultAmount: BigDecimal): TransactionStatus {

        val transactionStatus = SuccessTransaction(vaultAmount.plus(transactionAmount))

        val transaction = Transaction(accountId = accountId,
                transactionType = "CREDIT",
                amount = transactionAmount,
                status = transactionStatus.status()
        )
        transaction.save()

        return transactionStatus
    }
}

