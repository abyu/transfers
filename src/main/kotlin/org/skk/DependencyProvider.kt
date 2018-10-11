package org.skk

import org.skk.resource.Accounts
import org.skk.resource.Health
import org.skk.resource.Transfer

object DependencyProvider {

    fun healthResource() : Health {
        return Health()
    }

    fun transferResource() : Transfer {
        return Transfer()
    }

    fun accountsResource() : Accounts {
        return Accounts()
    }
}