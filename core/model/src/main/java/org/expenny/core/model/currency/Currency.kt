package org.expenny.core.model.currency

import org.expenny.core.model.profile.Profile
import java.math.BigDecimal
import kotlin.Long

data class Currency(
    val id: Long,
    val isMain: Boolean,
    val profile: Profile,               // PLN PLN PLN
    val unit: CurrencyUnit,             // EUR USD PLN
    val baseToQuoteRate: BigDecimal,    // 0.20 0.24 1
    val quoteToBaseRate: BigDecimal,    // 4.50 4.43 1
    val isSubscribedToRateUpdates: Boolean
)

