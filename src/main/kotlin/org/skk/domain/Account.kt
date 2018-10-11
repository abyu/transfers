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


}

interface TransactionStatus {
    fun isSuccess(): Boolean
    fun resultAmount(): BigDecimal
    fun failureReason(): String
    fun status(): String
}

