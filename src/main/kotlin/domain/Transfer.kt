package org.skk.domain

import java.math.BigDecimal

class TransferParams {
    var transfer = this

    lateinit var fromAccount: Account
    lateinit var toAccount: Account
    lateinit var amount: BigDecimal

    infix fun from(account: Account): TransferParams {
        fromAccount = account
        return this

    }
    infix fun to(account: Account): TransferParams {
        toAccount = account
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
        val status = it.fromAccount.debit(transferParams.amount)
        if (status.status == "SUCCESS") {
            it.toAccount.credit(transferParams.amount)
        }
        return status
    }
}