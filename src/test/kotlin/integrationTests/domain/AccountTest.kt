package integrationTests.domain

import assertk.assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import io.ebean.Ebean
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.skk.domain.AccountEntity
import org.skk.domain.VaultEntry
import org.skk.service.Account
import org.skk.service.AccountCreationException
import org.skk.service.AccountNotFoundException
import org.skk.service.Vault
import java.math.BigDecimal

class AccountTest {

    @Before
    fun setUp() {
        Ebean.deleteAll(AccountEntity.all())
        Ebean.deleteAll(VaultEntry.all())
    }

    @Test
    fun `create an new account along with a vault`() {
        val account = Account(Vault())

        val newAccountId = account.createNew(name = "Dory", initialAmount = BigDecimal("200"))

        val dbAccountEntity = AccountEntity.byId(newAccountId)
        val dbVaultEntity = VaultEntry.findByAccountId(newAccountId)

        assert(dbAccountEntity).isNotNull {
            assert(it.actual.name).isEqualTo("Dory")
        }
        assert(dbVaultEntity).isNotNull {
            assert(it.actual.amount).isEqualTo(BigDecimal("200"))
        }
    }

    @Test
    fun `rollback account creation when setting up vault fails`() {
        val mockVault = mockk<Vault>()
        val account = Account(mockVault)
        every { mockVault.setUpFor(any(), any()) } throws Exception("Intentional exception")

        assert {
            account.createNew(name = "Dory", initialAmount = BigDecimal("200"))
        }.thrownError {
            isInstanceOf(AccountCreationException::class)
        }

        assert(AccountEntity.all()).hasSize(0)
        assert(VaultEntry.all()).hasSize(0)
    }

    @Test
    fun `get an account from the db for the given id`() {
        val mockVault = mockk<Vault>()
        val account = Account(mockVault)
        val accountEntity = AccountEntity(name = "Dory")
        accountEntity.save()

        val persistedAccount = account.getAccount(accountEntity.id)

        assert(persistedAccount).isNotNull {
            assert(it.actual.name).isEqualTo("Dory")
        }
    }

    @Test
    fun `throw AccountNotFound exception when no account with given id exists`() {
        val mockVault = mockk<Vault>()
        val account = Account(mockVault)

        assert {
            account.getAccount(10)
        }.thrownError { isInstanceOf(AccountNotFoundException::class) }
    }
}