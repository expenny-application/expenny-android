package org.expenny.core.database.utils

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class BigDecimalConverter {

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input.isNullOrEmpty()) return BigDecimal.ZERO
        return BigDecimal(input)
    }

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toString() ?: BigDecimal.ZERO.toString()
    }
}

class LocalDateTimeConverter {

    @TypeConverter
    fun stringToLocalDateTime(input: String?): LocalDateTime? {
        return input?.let { LocalDateTime.parse(input) }
    }

    @TypeConverter
    fun localDateTimeToString(input: LocalDateTime?): String? {
        return input?.toString()
    }
}

class OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun stringToOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun offsetDateTimeToString(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}

class LocalDateConverter {

    @TypeConverter
    fun stringToLocalDate(input: String?): LocalDate? {
        return input?.let { LocalDate.parse(input) }
    }

    @TypeConverter
    fun localDateToString(input: LocalDate?): String? {
        return input?.toString()
    }
}

class ClosedLocalDateRangeConverter {

    @TypeConverter
    fun stringToClosedLocalDateRange(input: String?): ClosedRange<LocalDate>? {
        return if (input != null) {
            val (startDate, endDate) = input.split("..")
            val startLocalDate = LocalDate.parse(startDate)
            val endLocalDate = LocalDate.parse(endDate)

            startLocalDate.rangeTo(endLocalDate)
        } else null
    }

    @TypeConverter
    fun closedLocalDateRangeToString(input: ClosedRange<LocalDate>?): String? {
        val startDate = input?.start?.toString()
        val endDate = input?.endInclusive?.toString()

        return if (startDate != null && endDate != null) "$startDate..$endDate" else null
    }
}

class UriConverter {

    @TypeConverter
    fun stringToUri(input: String?): Uri? {
        return input?.toUri()
    }

    @TypeConverter
    fun uriToString(input: Uri?): String? {
        return input?.toString()
    }
}

class ListConverter {

    @TypeConverter
    fun listToString(input: List<String>?): String {
        return if (input.isNullOrEmpty()) "" else input.joinToString(separator = ";").trim()
    }

    @TypeConverter
    fun stringToList(input: String?): List<String> {
        return if (input.isNullOrBlank()) emptyList() else input.split(";")
    }
}