package org.expenny.core.common.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDate.atNow(): LocalDateTime {
    return this.atTime(LocalDateTime.now().toLocalTime())
}

fun LocalDate.atEndOfDay(): LocalDateTime {
    return this.atTime(LocalTime.MAX)
}