package org.expenny.core.ui.data

import java.math.BigDecimal

data class AmountUi(
    val value: BigDecimal,
    val displayValue: String = value.toPlainString(),
)