package org.expenny.core.database.utils

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.*

class BigDecimalConverter {

    @TypeConverter
    fun stringToBigDecimal(input: String?): BigDecimal {
        if (input == null || input.isEmpty()) return BigDecimal.ZERO
        return BigDecimal(input)
    }

    @TypeConverter
    fun bigDecimalToString(input: BigDecimal?): String {
        return input?.toString() ?: BigDecimal.ZERO.toString()
    }
}

class LocalDateTimeConverter {

    @TypeConverter
    fun longToLocalDateTime(input: Long?): LocalDateTime? {
        // Converts UTC epoch to JVM type applying current device timezone
        return input?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(input), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun localDateTimeToLong(input: LocalDateTime?): Long? {
        // Converts JVM date in UTC epoch
        return input
            ?.atZone(ZoneId.systemDefault())
            ?.withZoneSameInstant(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
    }
}

class LocalDateConverter {

    @TypeConverter
    fun longToLocalDate(input: Long?): LocalDate? {
        // Converts UTC epoch to JVM type applying current device timezone
        return input?.let {
            LocalDate.ofInstant(Instant.ofEpochMilli(input), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun localDateToLong(input: LocalDate?): Long? {
        // Converts JVM date in UTC epoch
        return input
            ?.atStartOfDay()
            ?.atZone(ZoneId.systemDefault())
            ?.withZoneSameInstant(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
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