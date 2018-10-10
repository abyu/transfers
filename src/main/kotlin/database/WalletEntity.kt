package org.skk.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class WalletEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WalletEntity>(WalletTable)
}