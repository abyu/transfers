package org.skk.exceptions

import java.lang.RuntimeException

data class IllegalMoneyOperationException(val param1: String, val param2: String, val operation: String) : RuntimeException("Cannot perform $param1 $operation $param2")