package org.expenny.core.ui.data.ui

import java.math.BigDecimal

data class AmountUi(
    val value: BigDecimal,
    val displayValue: String = "",
)