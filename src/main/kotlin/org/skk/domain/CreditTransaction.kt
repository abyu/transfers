package org.skk.domain

import java.math.BigDecimal

class CreditTransaction(private val accountId: Long, private val transactionAmount: BigDecimal) {
    fun execute(vaultAmount: BigDecimal): TransactionStatus {

        val transactionStatus = SuccessTransaction(vaultAmount + transactionAmount)

        val transaction = Transaction(accountId = accountId,
                transactionType = "CREDIT",
                amount = transactionAmount,
                status = transactionStatus.status()
        )
        transaction.save()

        return transactionStatus
    }

}
