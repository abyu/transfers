package org.skk.domain

import java.math.BigDecimal

class CreditTransaction(override val accountId: Long, private val transactionAmount: BigDecimal) : TransactionOperation {
    override fun execute(vaultAmount: BigDecimal): TransactionStatus {

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

interface TransactionOperation {
    val accountId: Long
    fun execute(vaultAmount: BigDecimal) : TransactionStatus
}