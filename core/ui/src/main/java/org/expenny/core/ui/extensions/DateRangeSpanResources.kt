package org.expenny.core.ui.extensions

import androidx.annotation.ArrayRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import org.expenny.core.common.types.DateRangeSpan
import org.expenny.core.resources.R

val DateRangeSpan.labelResId: Int
    @ArrayRes
    get() = R.array.date_range_span

val DateRangeSpan.label: String
    @Composable
    get() = stringArrayResource(R.array.date_range_span).get(
        when (this) {
            is DateRangeSpan.Day -> 0
            is DateRangeSpan.Week -> 1
            is DateRangeSpan.Month -> 2
            is DateRangeSpan.Quarter -> 3
            is DateRangeSpan.Year -> 4
        }
    )