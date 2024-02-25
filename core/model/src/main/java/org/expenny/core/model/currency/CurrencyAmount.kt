package org.expenny.core.model.currency

import org.expenny.core.common.extensions.isZero
import org.expenny.core.common.extensions.toCurrencyString
import org.joda.money.Money
import java.math.BigDecimal
import java.math.RoundingMode

data class CurrencyAmount(
    private val amountValue: BigDecimal,
    var currency: Currency,
) {
    val moneyValue = Money.of(org.joda.money.CurrencyUnit.of(currency.unit.code), amountValue)
    val value: BigDecimal = moneyValue.amount

    fun convertTo(targetCurrency: Currency): CurrencyAmount {
        if (currency.unit.code == targetCurrency.unit.code) {
            return this
        }
        val rate = targetCurrency.baseToQuoteRate.divide(
            currency.baseToQuoteRate,
            targetCurrency.unit.scale,
            RoundingMode.HALF_UP
        )
        val moneyResult = moneyValue.convertedTo(
            org.joda.money.CurrencyUnit.of(targetCurrency.unit.code),
            rate,
            RoundingMode.HALF_UP
        )
        return CurrencyAmount(moneyResult.amount, targetCurrency)
    }

    fun negated(): CurrencyAmount {
        if (value.isZero()) {
            return this
        }
        return copy(amountValue = value.negate())
    }

    fun abs(): CurrencyAmount {
        return copy(amountValue = value.abs())
    }

    override fun toString(): String {
        return buildString {
            append(value.toCurrencyString())
            append(' ')
            append(currency.unit.code)
        }
    }
}

