package org.skk.exceptions

import java.lang.RuntimeException

data class AccountNotFoundException(val id: Long) : RuntimeException("Account with id $id not found")