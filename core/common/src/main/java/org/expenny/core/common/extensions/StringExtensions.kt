package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.math.pow

fun String.asMonetaryBigDecimal(scale: Int): BigDecimal {
    val bigDecimal = ifEmpty { "0" }.toBigDecimal().setScale(scale)
    return if (bigDecimal.isZero()) bigDecimal else bigDecimal.divide(BigDecimal(1 * 10.0.pow(scale)))
}

fun String.asArgb(): Int? {
    if (this[0] == '#') {
        // Use a long to avoid rollovers on #ffXXXXXX
        var color: Long = this.substring(1).toLong(16)
        if (this.length == 7) {
            // Set the alpha value
            color = color or -0x1000000
        } else {
            require(this.length == 9) { "Unknown color" }
        }
        return color.toInt()
    }
    return null
}

fun String.capitalCase(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

val Int.asColorHex: String
    get() = String.format("#%08X", 0xFFFFFFFF and this.toLong())

fun String.join(vararg parts: String): String {
    return listOf(this, *parts).joinToString(separator = " • ")
}

fun String.toLocalDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))
    } catch (e: DateTimeParseException) {
        null
    }
}

fun String.toLocalTime(): LocalTime? {
    return try {
        LocalTime.parse(this, DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_FORMAT))
    } catch (e: DateTimeParseException) {
        null
    }
}
