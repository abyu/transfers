package org.skk.exceptions

import java.lang.RuntimeException

data class TransferFailedException(val msg: String) : RuntimeException(msg)