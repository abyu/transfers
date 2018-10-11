package org.skk.service

import org.skk.domain.VaultEntry

class Vault {
    fun execute(transactionOperation: TransactionOperation): TransactionStatus {
        val accountId = transactionOperation.accountId
        val vaultEntry = VaultEntry.findByAccountId(accountId)

        val transactionStatus = vaultEntry?.let {
            val status = transactionOperation.execute(it.amount)

            status.whenSuccess {
                it.updateAmount(status.resultAmount())
                it.save()
            }

        } ?: FailedTransaction("No vault with $accountId exists")

        return transactionStatus
    }
}


