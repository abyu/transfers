package org.skk.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class AccountEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AccountEntity>(AccountsTable)

    var name by AccountsTable.name
    var wallet by WalletEntity referencedOn AccountsTable.wallet
}

