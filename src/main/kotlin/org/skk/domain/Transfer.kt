package org.skk.domain

import org.skk.service.SuccessTransaction
import org.skk.service.TransactionStatus
import java.math.BigDecimal

class TransferParams {
    var transfer = this

    lateinit var fromAccount: AccountEntity
    lateinit var toAccount: AccountEntity
    lateinit var amount: BigDecimal

    infix fun from(accountEntity: AccountEntity): TransferParams {
        fromAccount = accountEntity
        return this

    }
    infix fun to(accountEntity: AccountEntity): TransferParams {
        toAccount = accountEntity
        return this

    }
    infix fun amount(amount: BigDecimal): TransferParams {
        this.amount = amount
        return this
    }
}

fun paymentTransaction(block : TransferParams.() -> TransferParams): TransactionStatus {
    val transferParams = block(TransferParams())

    transferParams.let{
//        val status = it.fromAccount.debit(transferParams.amount)
//        if (status.status == "SUCCESS") {
//            it.toAccount.credit(transferParams.amount)
//        }
        return SuccessTransaction(BigDecimal.ZERO)
    }
}