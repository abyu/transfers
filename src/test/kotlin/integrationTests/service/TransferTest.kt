package integrationTests.service

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.ebean.Ebean
import org.junit.Test
import org.skk.domain.VaultEntry
import org.skk.service.Transfer
import org.skk.service.TransferFailedException
import org.skk.service.TransferParams
import org.skk.service.Vault
import java.math.BigDecimal

class TransferTest {

    @Test
    fun `execute a successful transfer and persists in the db`() {
        val vaultForSender = VaultEntry(accountId = 2, amount = BigDecimal("1000"))
        val vaultForReceiver = VaultEntry(accountId = 4, amount = BigDecimal("500"))
        withDbEntries(listOf(vaultForSender, vaultForReceiver)) {
            val transfer = Transfer(Vault())

            transfer.forParams(TransferParams(
                    senderAccountId = 2,
                    receiverAccountId = 4,
                    amount = BigDecimal("300")))

            val updatedSenderVault = VaultEntry.byId(vaultForSender.id)
            val updatedReceiverVault = VaultEntry.byId(vaultForReceiver.id)

            assert(updatedSenderVault?.amount).isEqualTo(BigDecimal("700"))
            assert(updatedReceiverVault?.amount).isEqualTo(BigDecimal("800"))
        }
    }

    @Test
    fun `no vaults are updated when the sender does not have enough funds`() {
        val vaultForSender = VaultEntry(accountId = 2, amount = BigDecimal("400"))
        val vaultForReceiver = VaultEntry(accountId = 4, amount = BigDecimal("500"))
        withDbEntries(listOf(vaultForSender, vaultForReceiver)) {

            val transfer = Transfer(Vault())

            transfer.forParams(TransferParams(
                    senderAccountId = 2,
                    receiverAccountId = 4,
                    amount = BigDecimal("700")))

            val updatedSenderVault = VaultEntry.byId(vaultForSender.id)
            val updatedReceiverVault = VaultEntry.byId(vaultForReceiver.id)

            assert(updatedSenderVault?.amount).isEqualTo(BigDecimal("400"))
            assert(updatedReceiverVault?.amount).isEqualTo(BigDecimal("500"))
        }
    }

    @Test
    fun `restore sender vault when credit to receiver fails`() {
        val vaultForSender = VaultEntry(accountId = 2, amount = BigDecimal("400"))
        withDbEntries(listOf(vaultForSender)) {

            val transfer = Transfer(Vault())

            assert {
                transfer.forParams(TransferParams(
                        senderAccountId = 2,
                        receiverAccountId = 4,
                        amount = BigDecimal("100")))
            }.thrownError { isInstanceOf(TransferFailedException::class) }

            val updatedSenderVault = VaultEntry.byId(vaultForSender.id)

            assert(updatedSenderVault?.amount).isEqualTo(BigDecimal("400"))
        }
    }
}

fun withDbEntries(entries: List<Any>, block: () -> Unit) {
    Ebean.saveAll(entries)
    try {
        block()
    } finally {
        Ebean.deleteAll(entries)
    }
}