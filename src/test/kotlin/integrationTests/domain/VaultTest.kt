package integrationTests.domain

import assertk.assert
import assertk.assertions.isEqualTo
import io.ebean.Ebean
import org.junit.Test
import org.skk.service.CreditTransaction
import org.skk.service.DebitTransaction
import org.skk.service.Vault
import org.skk.domain.VaultEntry
import java.math.BigDecimal

class VaultTest {

    @Test
    fun `executes the debit transaction with the account's vault amount`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("200"))
        withVaultEntry(vaultEntry) {
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = BigDecimal("100"))

            val transactionStatus = vault.execute(debitTransaction)

            assert(transactionStatus.status()).isEqualTo("SUCCESS")
        }
    }

    @Test
    fun `the new amount from the successful debit transaction is persisted to the account's vault amount`() {
        val vault = Vault()
        val vaultEntry = VaultEntry(accountId = 2, amount = BigDecimal("200"))
        withVaultEntry(vaultEntry) {
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = BigDecimal("100"))

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
            val debitTransaction = DebitTransaction(accountId = 2, transactionAmount = BigDecimal("150"))

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
            val creditTransaction = CreditTransaction(accountId = 2, transactionAmount = BigDecimal("100"))

            val transactionStatus = vault.execute(creditTransaction)

            val updatedVaultEntry = VaultEntry.byId(it.id)
            assert(transactionStatus.status()).isEqualTo("SUCCESS")
            assert(updatedVaultEntry?.amount).isEqualTo(BigDecimal("300"))
        }
    }

    private fun withVaultEntry(vaultEntry: VaultEntry, block: (VaultEntry) -> Unit) {
        Ebean.save(vaultEntry)
        block(vaultEntry)
        Ebean.delete(vaultEntry)
    }
}