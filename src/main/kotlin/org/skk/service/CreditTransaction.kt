package org.skk.service

import org.skk.domain.Money
import org.skk.domain.Transaction

class CreditTransaction(override val accountId: Long, private val transactionAmount: Money) : TransactionOperation {
    override fun execute(vaultAmount: Money): TransactionStatus {

        val resultingAmount = vaultAmount plus transactionAmount
        val transactionStatus = SuccessTransaction(resultingAmount.value)

        val transaction = Transaction(accountId = accountId,
                transactionType = "CREDIT",
                amount = transactionAmount.value,
                status = transactionStatus.status()
        )
        transaction.save()

        return transactionStatus
    }
}

