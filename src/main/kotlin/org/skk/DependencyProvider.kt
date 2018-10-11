package org.skk

import org.skk.resource.Accounts
import org.skk.resource.Health
import org.skk.resource.Transfers
import org.skk.service.Account
import org.skk.service.Transfer
import org.skk.service.Vault

object DependencyProvider {

    fun healthResource() : Health {
        return Health()
    }

    fun transferResource() : Transfers {
        return Transfers(getTransferService())
    }

    fun accountsResource() : Accounts {
        return Accounts(getAccountService())
    }

    fun getAccountService() : Account {
        return Account(getVault())
    }

    private fun getVault() : Vault {
        return Vault()
    }

    fun getTransferService(): Transfer {
        return Transfer(getVault())
    }
}