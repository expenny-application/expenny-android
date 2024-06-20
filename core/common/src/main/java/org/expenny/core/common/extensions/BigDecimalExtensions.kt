package org.expenny.core.common.extensions


import org.expenny.core.common.utils.Constants
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun BigDecimal.asRawString() = toString().filter { it.isDigit() }.let {
    if (it.length > 1) it.trimStart('0') else it
}

fun BigDecimal.setScaleRoundingDown(scale: Int): BigDecimal {
    return setScale(scale, RoundingMode.DOWN)
}

fun BigDecimal.toCurrencyAmountString(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val decimalFormatSymbols = (formatter as DecimalFormat).decimalFormatSymbols

    decimalFormatSymbols.currencySymbol = ""
    formatter.decimalFormatSymbols = decimalFormatSymbols
    formatter.minimumFractionDigits = scale()
    formatter.maximumFractionDigits = scale()

    return formatter.format(this).trim()
}

fun BigDecimal.percentageOf(total: BigDecimal): BigDecimal {
    if (total.isZero()) return total.setScale(0)

    return this.multiply(BigDecimal(100)).divide(total, 2, RoundingMode.HALF_UP).let {
        if (it.isWhole()) it.setScale(0) else it
    }
}

fun BigDecimal.invert(
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
    scale: Int = scale()
): BigDecimal {
    return try {
        BigDecimal.ONE.divide(this, scale, roundingMode)
    } catch (e: ArithmeticException) {
        return BigDecimal.ZERO.setScale(scale)
    }
}

fun BigDecimal.convertBy(rate: BigDecimal): BigDecimal {
    if (rate == BigDecimal.ONE) return this

    return try {
        this.multiply(rate).setScale(scale(), RoundingMode.HALF_UP)
    } catch (e: Exception) {
        this
    }
}

fun BigDecimal.conversionRateOf(amount: BigDecimal): BigDecimal {
    return try {
        divide(amount, Constants.CURRENCY_RATE_SCALE, RoundingMode.HALF_UP)
    } catch (e: ArithmeticException) {
        return BigDecimal.ZERO.setScale(Constants.CURRENCY_RATE_SCALE)
    }
}

fun BigDecimal.isWhole(): Boolean {
    return signum() == 0 || scale() <= 0 || stripTrailingZeros().scale() <= 0
}

fun BigDecimal.isZero(): Boolean {
    return this.compareTo(BigDecimal.ZERO) == 0
}

fun BigDecimal.isNegative(): Boolean {
    return this < BigDecimal.ZERO
}

fun BigDecimal.isPositive(): Boolean {
    return this >= BigDecimal.ZERO
}