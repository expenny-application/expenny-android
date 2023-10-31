package org.expenny.core.domain.usecase

import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyAmount
import java.math.BigDecimal
import javax.inject.Inject

class GetCurrencyAmountSumUseCase @Inject constructor() {

    operator fun invoke(currencyAmounts: List<CurrencyAmount>, targetCurrency: Currency): CurrencyAmount {
        val sum = currencyAmounts.sumByDecimal { it.convertTo(targetCurrency).value }
        return CurrencyAmount(targetCurrency, sum)
    }
}