package org.skk.service

import assertk.all
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.ebean.EbeanServer
import io.ebean.MockiEbean
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.skk.domain.AccountEntity
import java.math.BigDecimal

class AccountTest {

    @Test
    fun `create a new account along with a vault`() {
        val mockVault = mockk<Vault>(relaxed = true)
        val account = Account(mockVault)
        val mockEbeanServer = mockk<EbeanServer>()
        val slot = slot<AccountEntity>()
        every { mockEbeanServer.save(capture(slot)) } just runs

        MockiEbean.runWithMock(mockEbeanServer) {
            val newAccountId = account.createNew(name = "Nemo", initialAmount = BigDecimal("200"))

            assert(slot.isCaptured).isTrue()
            assert(newAccountId).isEqualTo(slot.captured.id)
            assert(slot.captured).all{
                assert(actual.name).isEqualTo("Nemo")
            }
            verify { mockVault.setUpFor(slot.captured.id, BigDecimal("200")) }
        }
    }
}