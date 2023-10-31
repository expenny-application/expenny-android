package org.expenny.core.common.types

import org.threeten.extra.LocalDateRange
import java.time.LocalDate

enum class TimeSpan {
    Last7Days,
    Last1Month,
    Last3Months,
    Last6Months,
    Last1Year,
    Last5Years;

    fun toLocalDateRange(): LocalDateRange {
        val today = LocalDate.now()

        return when (this) {
            Last7Days -> LocalDateRange.of(today.minusDays(7), today)
            Last1Month -> LocalDateRange.of(today.minusMonths(1), today)
            Last3Months -> LocalDateRange.of(today.minusMonths(3), today)
            Last6Months -> LocalDateRange.of(today.minusMonths(6), today)
            Last1Year -> LocalDateRange.of(today.minusYears(1), today)
            Last5Years -> LocalDateRange.of(today.minusYears(5), today)
        }
    }
}