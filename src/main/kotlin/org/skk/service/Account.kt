package org.skk.service

import io.ebean.annotation.Transactional
import io.ebean.annotation.TxType
import org.skk.domain.AccountEntity
import org.skk.exceptions.AccountCreationException
import org.skk.exceptions.AccountNotFoundException
import org.skk.exceptions.VaultNotFoundException
import org.skk.model.Account
import org.skk.model.AccountBalance
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

    fun getAccount(accountId: Long): Account {

        val accountEntity = AccountEntity.byId(accountId)

        return accountEntity?.let {
            Account(id = it.id, name = it.name)
        } ?: throw AccountNotFoundException(accountId)
    }

    fun getBalance(accountId: Long): AccountBalance {

        val userAccount = getAccount(accountId)
        val userAccountId = userAccount.id
        val vaultAmount = vault.getVaultAmount(userAccountId)

        return vaultAmount?.let {
            AccountBalance(accountId = userAccountId, balance = it)
        } ?: throw VaultNotFoundException(userAccountId)
    }
}
