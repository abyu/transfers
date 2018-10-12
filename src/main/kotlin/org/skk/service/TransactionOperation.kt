package org.skk.service

import org.skk.domain.Money
import java.math.BigDecimal

interface TransactionOperation {
    val accountId: Long
    fun execute(vaultAmount: Money) : TransactionStatus
}

class SuccessTransaction(private val resultingAmount: Money) : TransactionStatus {
    override fun whenSuccess(block: (TransactionStatus) -> Unit): TransactionStatus {
        block(this)
        return this
    }

    override fun whenFailed(block: (TransactionStatus) -> Unit): TransactionStatus {
        return this
    }

    override fun status(): String = "SUCCESS"

    override fun isSuccess() = true

    override fun resultAmount() = resultingAmount

    override fun failureReason(): String  = ""
}

class FailedTransaction(private val reason: String) : TransactionStatus {
    override fun whenSuccess(block: (TransactionStatus) -> Unit): TransactionStatus {
        return this
    }

    override fun whenFailed(block: (TransactionStatus) -> Unit): TransactionStatus {
        block(this)
        return this
    }

    override fun status() = "FAILED"

    override fun isSuccess() = false

    override fun resultAmount() = Money(BigDecimal.ZERO)

    override fun failureReason(): String = reason
}

interface TransactionStatus {
    fun isSuccess(): Boolean
    fun resultAmount(): Money
    fun failureReason(): String
    fun status(): String

    fun whenSuccess(block: (TransactionStatus) -> Unit) : TransactionStatus
    fun whenFailed(block: (TransactionStatus) -> Unit) : TransactionStatus
}