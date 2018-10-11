package org.skk.domain

import io.ebean.Finder
import io.ebean.Model
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="account")
data class AccountEntity(
        @Id val id: Long = 0,
        val name: String
       ) : Model() {

       companion object Find: AccountFinder()
}

open class AccountFinder : Finder<Long, AccountEntity>(AccountEntity::class.java)
