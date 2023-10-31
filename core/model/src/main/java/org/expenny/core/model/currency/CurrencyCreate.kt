package org.expenny.core.model.currency

import java.math.BigDecimal
import kotlin.Long

data class CurrencyCreate(
    val profileId: Long,
    val code: String,
    val baseToQuoteRate: BigDecimal
)
