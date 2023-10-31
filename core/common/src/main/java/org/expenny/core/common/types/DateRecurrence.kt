package org.expenny.core.common.types

import org.expenny.core.common.extensions.toMonthRange
import org.expenny.core.common.extensions.toQuarterRange
import org.expenny.core.common.extensions.toWeekRange
import org.expenny.core.common.extensions.toYearRange
import org.threeten.extra.LocalDateRange
import java.time.LocalDate

enum class DateRecurrence {
    Daily,
    Weekly,
    Monthly,
    Quarterly,
    Annually;

    fun currentDateRange(): LocalDateRange {
        val now = LocalDate.now()

        return when (this) {
            Daily -> LocalDateRange.ofClosed(now, now)
            Weekly -> now.toWeekRange()
            Monthly -> now.toMonthRange()
            Quarterly -> now.toQuarterRange()
            Annually -> now.toYearRange()
        }
    }
}