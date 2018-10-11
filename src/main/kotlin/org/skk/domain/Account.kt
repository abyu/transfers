package org.skk.domain

import io.ebean.Model
import org.skk.database.AccountEntity
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="account")
class Account(@Id val id: Int, val name: String) : Model() {


//    fun debit(amount: BigDecimal): TransactionStatus {
//        val newBalance = getBalance().minus(amount)
//
//        return newBalance.takeIf { it >= BigDecimal.ZERO }?.let{
//            balance = newBalance
//            TransactionStatus("SUCCESS", "")
//        } ?: TransactionStatus("FAILED", "Insufficient funds")
//
//    }
//
//    fun credit(amount: BigDecimal) {
//        balance = getBalance().plus(amount)
//
//    }
//
//    fun getBalance(): BigDecimal {
//        return accountEntity.wallet.amount
//    }
}

data class TransactionStatus(val status: String, val reason: String)

class Vault {

//    fun apply(transaction: Transaction) {
//        vaultEntry = VaultEntry.findByAccountId(transaction.accountId)
//        newVaultAmount = transaction.execute(vaultAmount)
//        vaultEntry.update(newVaultAmount)
//        vaultEntry.save()
//    }
}

class Transaction(@Id val id: Int, val type: String, val accountId: Int, val amount: BigDecimal) : Model() {

}
