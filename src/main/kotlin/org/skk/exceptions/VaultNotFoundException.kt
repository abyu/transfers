package org.skk.exceptions

import java.lang.RuntimeException

data class VaultNotFoundException(val id: Long) : RuntimeException("No vault found for account with id $id")