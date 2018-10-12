package org.skk.domain

import io.ebean.Finder
import io.ebean.Model
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name="vault")
@Entity
data class VaultEntry(
    @Id
    val id : Long = 0,
    val accountId: Long,
    var amount: BigDecimal) : Model() {

    fun updateMoney(money: Money) {
       this.amount = money.value
    }
    companion object Find: VaultFinder()
}

open class VaultFinder : Finder<Long, VaultEntry>(VaultEntry::class.java) {

    open fun findByAccountId(accountId: Long): VaultEntry? {
        return query().where().eq("accountId", accountId).findOne()
    }
}
