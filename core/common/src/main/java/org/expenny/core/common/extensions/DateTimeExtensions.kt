package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants
import org.threeten.extra.LocalDateRange
import org.threeten.extra.YearQuarter
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters

fun LocalDate.toDateString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))

fun LocalTime.toTimeString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_FORMAT))

fun LocalDateTime.toDateString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT))

fun LocalDateTime.toTimeString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_FORMAT))

fun LocalDateTime.toDateTimeString(): String = format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATETIME_FORMAT))
