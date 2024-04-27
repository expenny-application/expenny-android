package org.expenny.core.ui.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import org.expenny.core.common.extensions.isZero
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.pow

class ExpennyDecimalVisualTransformation(private val scale: Int) : VisualTransformation {
    private val symbols = (DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat).decimalFormatSymbols
    private val thousandsSeparator = symbols.groupingSeparator
    private val decimalSeparator = symbols.decimalSeparator
    private val zeroDigit = symbols.zeroDigit

    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text

        val formattedNumber = buildString {
            val intPart = input
                .dropLast(scale)
                .reversed()
                .chunked(3)
                .joinToString(separator = thousandsSeparator.toString())
                .reversed()
                .ifEmpty { zeroDigit.toString() }

            append(intPart)

            if (scale > 0) {
                val decimalSeparator = decimalSeparator
                val fractionPart = input.takeLast(scale).let {
                    if (it.length != scale) {
                        List(scale - it.length) { zeroDigit }
                            .joinToString("") + it
                    } else {
                        it
                    }
                }

                append(decimalSeparator)
                append(fractionPart)
            }
        }

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = FixedCursorOffsetMapping(
            contentLength = text.length,
            formattedContentLength = formattedNumber.length
        )

        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    companion object {

        fun formatToOutput(output: String, scale: Int): BigDecimal {
            return if (output.toBigDecimalOrNull() != null) {
                val bigDecimal = output.ifEmpty { "0" }.toBigDecimal().setScale(scale)
                if (bigDecimal.isZero()) {
                    bigDecimal
                } else {
                    bigDecimal.divide(BigDecimal(1 * 10.0.pow(scale)))
                }
            } else {
                BigDecimal.ZERO.setScale(scale)
            }
        }

        fun formatToInput(value: BigDecimal): String {
            return value.toPlainString().filter { it.isDigit() }.let {
                if (it.length > 1) it.trimStart('0') else it
            }
        }

        fun formatToOutput(value: String): String {
            if (!value.isDigitsOnly()) {
                return if (value.isEmpty()) "0" else ""
            } else {
                if (value.startsWith("0")) {
                    return if (value.length > 1) value.trimStart('0') else "0"
                }
            }
            return value
        }
    }
}