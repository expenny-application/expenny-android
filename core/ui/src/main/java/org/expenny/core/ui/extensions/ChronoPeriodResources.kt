package org.expenny.core.ui.extensions

import androidx.annotation.ArrayRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import org.expenny.core.common.types.ChronoPeriod
import org.expenny.core.resources.R

val ChronoPeriod.labelResId: Int
    @ArrayRes
    get() = R.array.chrono_period

val ChronoPeriod.label: String
    @Composable
    get() = stringArrayResource(R.array.chrono_period).get(
        when (this) {
            ChronoPeriod.Last24Hours -> 0
            ChronoPeriod.Last7Days -> 1
            ChronoPeriod.Last1Month -> 2
            ChronoPeriod.Last3Months -> 3
            ChronoPeriod.Last6Months -> 4
            ChronoPeriod.Last1Year -> 5
        }
    )
