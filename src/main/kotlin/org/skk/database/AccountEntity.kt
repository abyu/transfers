package org.skk.database

import io.ebean.Finder
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name="accounts")
data class AccountEntity(
    @Id
    val id : Int = 0,
    val name: String = ""
) {

    companion object Find: AccountFinder()
}

open class AccountFinder : Finder<Int, AccountEntity>(AccountEntity::class.java)
