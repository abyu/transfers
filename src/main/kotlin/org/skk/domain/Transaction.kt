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
