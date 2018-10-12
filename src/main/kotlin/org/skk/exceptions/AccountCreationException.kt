package org.skk.exceptions

import java.lang.RuntimeException

data class AccountCreationException(val msg: String?, val exception: Exception) : RuntimeException(msg, exception)