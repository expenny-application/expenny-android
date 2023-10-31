package org.expenny.core.ui.extensions

import androidx.annotation.ArrayRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import org.expenny.core.common.types.TimePeriod
import org.expenny.core.resources.R

val TimePeriod.labelResId: Int
    @ArrayRes
    get() = R.array.time_period

val TimePeriod.label: String
    @Composable
    get() = stringArrayResource(
        R.array.time_period
    ).get(
        when (this) {
            TimePeriod.Today -> 0
            TimePeriod.ThisWeek -> 1
            TimePeriod.ThisMonth -> 2
            TimePeriod.ThisQuarter -> 3
            TimePeriod.ThisYear -> 4
        }
    )
