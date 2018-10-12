package org.skk.service

import org.skk.domain.Money
import org.skk.domain.VaultEntry
import org.skk.domain.pounds
import java.math.BigDecimal

class Vault {
    fun execute(transactionOperation: TransactionOperation): TransactionStatus {
        val accountId = transactionOperation.accountId
        val vaultEntry = VaultEntry.findByAccountId(accountId)

        val transactionStatus = vaultEntry?.let {
            val status = transactionOperation.execute(Money(it.amount))

            status.whenSuccess {
                vaultEntry.updateMoney(status.resultAmount())
                vaultEntry.save()
            }

        } ?: FailedTransaction("No vault for account with Id: $accountId exists")

        return transactionStatus
    }

    fun setUpFor(accountId: Long, initialAmount: BigDecimal): TransactionStatus {
        VaultEntry.findByAccountId(accountId)?.let {
            return FailedTransaction("An existing Vault is present for account $accountId")
        }

        val vaultEntry = VaultEntry(accountId = accountId, amount = initialAmount)

        vaultEntry.save()

        return SuccessTransaction("0".pounds())
    }

    fun getVaultAmount(accountId: Long): BigDecimal? {
        return VaultEntry.findByAccountId(accountId)?.amount
    }
}


