package org.skk.domain

import io.ebean.Model
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "transactions")
class Transaction(@Id val id: Long = 0,
                  val transactionType: String,
                  val accountId: Long,
                  val amount: BigDecimal,
                  val status: String) : Model()

class DebitTransaction(val accountId: Long, val transactionAmount: BigDecimal) {
    fun execute(vaultAmount: BigDecimal): TransactionStatus {

        val result = transactionAmount.takeIf { it <= vaultAmount }?.let {
            SuccessTransaction(resultingAmount = vaultAmount - it)
        } ?: FailedTransaction(reason = "Insufficient funds")

        val transaction = Transaction(transactionType = "DEBIT", accountId = accountId, amount = transactionAmount, status = result.status())
        transaction.save()

        return result
    }
}

class SuccessTransaction(private val resultingAmount: BigDecimal) : TransactionStatus {
    override fun status(): String = "SUCCESS"

    override fun isSuccess() = true

    override fun resultAmount() = resultingAmount

    override fun failureReason(): String  = ""
}

class FailedTransaction(private val reason: String) : TransactionStatus {
    override fun status() = "FAILED"

    override fun isSuccess() = false

    override fun resultAmount() = BigDecimal.ZERO

    override fun failureReason(): String = reason
}