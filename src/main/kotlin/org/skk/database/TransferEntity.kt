package org.skk.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TransferEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TransferEntity>(TransfersTable)
}