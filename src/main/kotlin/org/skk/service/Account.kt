package org.skk.service

import io.ebean.annotation.Transactional
import io.ebean.annotation.TxType
import org.skk.domain.AccountEntity
import java.lang.RuntimeException
import java.math.BigDecimal

class Account(private val vault: Vault) {

    @Transactional(type = TxType.REQUIRES_NEW)
    fun createNew(name: String, initialAmount: BigDecimal) : Long {

        try {

            val account = AccountEntity(name = name)
            account.save()
            vault.setUpFor(accountId = account.id, initialAmount = initialAmount)

            return account.id
        } catch (ex: Exception) {
            throw AccountCreationException(ex.message, ex)
        }
    }

}

data class AccountCreationException(val msg: String?, val exception: Exception) : RuntimeException(msg, exception)
