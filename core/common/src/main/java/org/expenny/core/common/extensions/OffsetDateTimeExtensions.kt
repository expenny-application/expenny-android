package org.expenny.core.common.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

fun OffsetDateTime.toLocalDateTimeSystemDefault(): LocalDateTime {
    return atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
}

fun OffsetDateTime.toLocalDateSystemDefault(): LocalDate {
    return atZoneSameInstant(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDateTime.toOffsetDateTimeSystemDefault(): OffsetDateTime {
    return atZone(ZoneId.systemDefault()).toOffsetDateTime()
}

fun LocalDate.toOffsetDateTimeSystemDefault(): OffsetDateTime {
    return atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime()
}