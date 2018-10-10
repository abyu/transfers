package org.skk.domain

import java.math.BigDecimal

data class Account(val name: String) {
    private var balance = BigDecimal.ZERO

    fun debit(amount: BigDecimal): TransactionStatus {
        val newBalance = balance.minus(amount)

        return newBalance.takeIf { it >= BigDecimal.ZERO }?.let{
            balance = newBalance
            TransactionStatus("SUCCESS", "")
        } ?: TransactionStatus("FAILED", "Insufficient funds")

    }

    fun credit(amount: BigDecimal) {
        balance = balance.plus(amount)
    }

    fun getBalance(): BigDecimal {
        return balance
    }
}

data class TransactionStatus(val status: String, val reason: String)