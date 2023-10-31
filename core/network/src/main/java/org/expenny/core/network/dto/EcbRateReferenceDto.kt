package org.expenny.core.network.dto

import java.math.BigDecimal
import java.time.LocalDate

data class EcbRateReferenceDto(
    val quoteCurrency: String,
    val baseCurrency: String,
    val value: BigDecimal,
    val date: LocalDate
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EcbRateReferenceDto

        if (quoteCurrency != other.quoteCurrency) return false
        if (baseCurrency != other.baseCurrency) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quoteCurrency.hashCode()
        result = 31 * result + baseCurrency.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}
