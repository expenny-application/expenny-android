package org.expenny.core.database.utils

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.*

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
    fun longToLocalDateTime(input: Long?): LocalDateTime? {
        // Converts utc epoch to local date time
        return input?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(input), ZoneOffset.systemDefault())
        }
    }

    @TypeConverter
    fun localDateTimeToLong(input: LocalDateTime?): Long? {
        // Converts local date time to utc epoch
        return input?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}

class LocalDateConverter {

    @TypeConverter
    fun longToLocalDate(input: Long?): LocalDate? {
        // Converts utc epoch to local date
        return input?.let {
            LocalDate.ofInstant(Instant.ofEpochMilli(input), ZoneOffset.systemDefault())
        }
    }

    @TypeConverter
    fun localDateToLong(input: LocalDate?): Long? {
        // Converts local date to utc epoch
        return input?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
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