package org.skk.domain

import java.math.BigDecimal

data class Account(val name: String) {
    private var balance = BigDecimal.ZERO

    fun debit(amount: BigDecimal) {
        balance = balance.minus(amount)
    }

    fun credit(amount: BigDecimal) {
        balance = balance.plus(amount)

    }

    fun getBalance() : BigDecimal {
        return balance
    }
}
