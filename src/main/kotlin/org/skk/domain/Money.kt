package org.skk.domain

import org.skk.exceptions.IllegalMoneyOperationException
import java.math.BigDecimal

data class Money(val value: BigDecimal) {
    infix fun plus(addMoney: Money): Money {
        val newValue = value.plus(addMoney.value)

        return Money(newValue)
    }

    infix fun minus(subtractMoney: Money): Money {
        val newValue = value.minus(subtractMoney.value)

        newValue.takeIf { it < BigDecimal.ZERO }?.let {
            throw IllegalMoneyOperationException(value.toPlainString(), subtractMoney.value.toPlainString(), "minus")
        }

        return Money(newValue)
    }

    infix fun isLessThan(anotherMoney: Money): Boolean {
        return value < anotherMoney.value
    }
}

fun String.pounds(): Money {
    return Money(BigDecimal(this))
}