package org.expenny.core.model.common

import java.math.BigDecimal
import java.time.LocalDate

data class DatedAmount(
    val amount: BigDecimal,
    val date: LocalDate
)
