package org.expenny.core.common.types

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

enum class ChronoPeriod {
    Last24Hours,
    Last7Days,
    Last1Month,
    Last3Months,
    Last6Months,
    Last1Year;

    fun dateTimeRange(): ClosedRange<LocalDateTime> {
        return LocalDateTime.now().let { now ->
            when (this) {
                Last24Hours -> now.minus(24, ChronoUnit.HOURS)
                Last7Days -> now.minus(7, ChronoUnit.DAYS)
                Last1Month -> now.minus(1, ChronoUnit.MONTHS)
                Last3Months -> now.minus(3, ChronoUnit.MONTHS)
                Last6Months -> now.minus(6, ChronoUnit.MONTHS)
                Last1Year -> now.minus(1, ChronoUnit.YEARS)
            }.rangeTo(now)
        }
    }
}