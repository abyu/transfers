package integrationTests.domain

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import integrationTests.service.withDbEntries
import io.ebean.Ebean
import org.junit.Before
import org.junit.Test
import org.skk.domain.VaultEntry
import java.math.BigDecimal

class VaultEntryTest {

    @Before
    fun setUp() {
        Ebean.deleteAll(VaultEntry.all())
    }

    @Test
    fun `find a vault for the given account id`() {
        val vaultEntryForAccount1 = VaultEntry(accountId = 1, amount = BigDecimal(100))
        val vaultEntryForAccount2 = VaultEntry(accountId = 2, amount = BigDecimal(100))

        withDbEntries(listOf(vaultEntryForAccount1, vaultEntryForAccount2)) {
            val dbEntry = VaultEntry.findByAccountId(2)

            assert(dbEntry).isEqualTo(vaultEntryForAccount2)
        }
    }

    @Test
    fun `return null when no vault is found for the given account id`() {
        val vaultEntryForAccount1 = VaultEntry(accountId = 1, amount = BigDecimal(100))
        val vaultEntryForAccount2 = VaultEntry(accountId = 2, amount = BigDecimal(100))

        withDbEntries(listOf(vaultEntryForAccount1, vaultEntryForAccount2)) {
            val dbEntry = VaultEntry.findByAccountId(4)

            assert(dbEntry).isNull()
        }
    }
}
