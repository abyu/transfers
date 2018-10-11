package org.skk.service

import io.ebean.annotation.Transactional
import io.ebean.annotation.TxType
import java.lang.RuntimeException
import java.math.BigDecimal

class Transfer(private val vault: Vault) {

    @Transactional(type = TxType.REQUIRES_NEW, rollbackFor = [TransferFailedException::class])
    fun forParams(transferParams: TransferParams): TransactionStatus {

        val debitTransaction = DebitTransaction(transferParams.senderAccountId, transactionAmount = transferParams.amount)
        val creditTransaction = CreditTransaction(transferParams.receiverAccountId, transactionAmount = transferParams.amount)

        val debitStatus = vault.execute(debitTransaction)
        debitStatus.whenSuccess { _ ->
            val creditStatus = vault.execute(creditTransaction)

            creditStatus.whenFailed {
                throw TransferFailedException(it.failureReason())
            }
        }

        return debitStatus
    }
}

data class TransferParams(val senderAccountId: Long, val receiverAccountId: Long, val amount: BigDecimal)

data class TransferFailedException(val msg: String) : RuntimeException(msg)