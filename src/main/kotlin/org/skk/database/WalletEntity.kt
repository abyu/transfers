package org.skk.database

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name="wallet")
@Entity
data class WalletEntity(
    @Id
    val id : Int,
    val amount: BigDecimal
)