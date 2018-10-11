package org.skk.database

import org.jetbrains.exposed.dao.IntIdTable

object AccountsTable : IntIdTable(name = "accounts") {
    val name = varchar("name", 200)
    val wallet = reference("wallet", WalletTable)
}

object TransactionsTable : IntIdTable(name = "transactions")

object WalletTable : IntIdTable(name = "wallet")

object TransfersTable : IntIdTable(name = "transfers")

fun getAllTables() = arrayOf(AccountsTable, TransactionsTable, WalletTable, TransfersTable)