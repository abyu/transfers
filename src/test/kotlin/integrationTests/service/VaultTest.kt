package integrationTests.service

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.ebean.Ebean
import org.junit.Test
import org.skk.domain.AccountEntity
import org.skk.service.CreditTransaction
import org.skk.service.DebitTransaction
import org.skk.service.Vault
import org.skk.domain.VaultEntry
import org.skk.domain.pounds
import java.math.BigDecimal

class VaultTest {

    @Test
    fun `executes the debit transaction with the account's vault amount`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("200"))
        withVaultEntry(vaultEntry) {
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = "100".pounds())

            val transactionStatus = vault.execute(debitTransaction)

            assert(transactionStatus.status()).isEqualTo("SUCCESS")
        }
    }

    @Test
    fun `the new amount from the successful debit transaction is persisted to the account's vault amount`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("200"))
        withVaultEntry(vaultEntry) {
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = "100".pounds())

            val transactionStatus = vault.execute(debitTransaction)

            val updatedVaultEntry = VaultEntry.byId(vaultEntry.id)
            assert(transactionStatus.status()).isEqualTo("SUCCESS")
            assert(updatedVaultEntry?.amount).isEqualTo(BigDecimal("100"))
        }
    }

    @Test
    fun `do not update the vault when the transaction returns a failure result`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("100"))
        withVaultEntry(vaultEntry) {
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = "150".pounds())

            val transactionStatus = vault.execute(debitTransaction)

            val updatedVaultEntry = VaultEntry.byId(it.id)
            assert(transactionStatus.status()).isEqualTo("FAILED")
            assert(updatedVaultEntry?.amount).isEqualTo(BigDecimal("100"))
        }
    }

    @Test
    fun `executes the credit transaction updating the account's vault amount`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("200"))
        withVaultEntry(vaultEntry) {
            val creditTransaction = CreditTransaction(accountId = 2, transactionAmount = "100".pounds())

            val transactionStatus = vault.execute(creditTransaction)

            val updatedVaultEntry = VaultEntry.byId(it.id)
            assert(transactionStatus.status()).isEqualTo("SUCCESS")
            assert(updatedVaultEntry?.amount).isEqualTo(BigDecimal("300"))
        }
    }

    @Test
    fun `setup a vault for the given account id with given amount`() {
        val vault = Vault()
        val accountEntry = AccountEntity(name = "Nemo")
        withDbEntries(listOf(accountEntry)) {
            vault.setUpFor(accountId = accountEntry.id, initialAmount = BigDecimal("200"))

            val vaultEntry = VaultEntry.findByAccountId(accountId = accountEntry.id)

            assert(vaultEntry).isNotNull()
            assert(vaultEntry?.amount).isEqualTo(BigDecimal("200"))
        }
    }

    @Test
    fun `do nothing when the vault already exists`() {
        val vault = Vault()
        val accountEntry = AccountEntity(name = "Nemo")
        Ebean.save(accountEntry)
        val vaultEntry = VaultEntry(accountId = accountEntry.id, amount = BigDecimal("200"))

        withDbEntries(listOf(accountEntry, vaultEntry)) {
            vault.setUpFor(accountId = accountEntry.id, initialAmount = BigDecimal("800"))

            val dbVaultEntry = VaultEntry.findByAccountId(accountId = accountEntry.id)

            assert(dbVaultEntry).isNotNull()
            assert(dbVaultEntry?.amount).isEqualTo(BigDecimal("200"))
        }
    }

    @Test
    fun `returns the available vault amount for the given account id`() {
        val vault = Vault()
        val accountEntry = AccountEntity(name = "Nemo")
        Ebean.save(accountEntry)
        val vaultEntry = VaultEntry(accountId = accountEntry.id, amount = BigDecimal("200"))

        withDbEntries(listOf(accountEntry, vaultEntry)) {
            val vaultAmount = vault.getVaultAmount(accountId = accountEntry.id)

            assert(vaultAmount).isEqualTo(BigDecimal("200"))
        }
    }

    @Test
    fun `returns null when no vault found for the given account id`() {
        val vault = Vault()
        val accountEntry = AccountEntity(name = "Nemo")

        withDbEntries(listOf(accountEntry)) {
            val vaultAmount = vault.getVaultAmount(accountId = accountEntry.id)

            assert(vaultAmount).isNull()
        }
    }

    private fun withVaultEntry(vaultEntry: VaultEntry, block: (VaultEntry) -> Unit) {
        Ebean.save(vaultEntry)
        block(vaultEntry)
        Ebean.delete(vaultEntry)
    }
}