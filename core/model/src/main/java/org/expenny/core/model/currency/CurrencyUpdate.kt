package org.expenny.core.model.currency

import java.math.BigDecimal
import kotlin.Long

data class CurrencyUpdate(
    val id: Long,
    val baseToQuoteRate: BigDecimal,
    val isSubscribedToRateUpdates: Boolean
)
