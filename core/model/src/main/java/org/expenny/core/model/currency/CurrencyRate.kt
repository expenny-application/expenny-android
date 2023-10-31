package org.expenny.core.model.currency

import java.math.BigDecimal
import java.time.LocalDate

data class CurrencyRate(
    val baseCurrencyUnit: CurrencyUnit,
    val quoteCurrencyUnit: CurrencyUnit,
    val rate: BigDecimal,
    val date: LocalDate,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyRate

        if (quoteCurrencyUnit.code != other.quoteCurrencyUnit.code) return false
        if (baseCurrencyUnit.code != other.baseCurrencyUnit.code) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quoteCurrencyUnit.code.hashCode()
        result = 31 * result + baseCurrencyUnit.code.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}
