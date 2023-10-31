package org.expenny.core.common.extensions

import org.expenny.core.common.utils.Constants.DEFAULT_DATE_FORMAT
import org.expenny.core.common.utils.Constants.DEFAULT_TIME_FORMAT
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.toSystemLocalDate(): LocalDate = atZone(ZoneId.systemDefault()).toLocalDate()

fun Instant.toSystemLocalTime(): LocalTime = atZone(ZoneId.systemDefault()).toLocalTime()

fun Instant.toDefaultString(): String = toSystemLocalDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT))

fun Instant.toTimeString(): String = toSystemLocalTime().format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT))