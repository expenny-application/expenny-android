package org.expenny.core.ui.extensions

import androidx.annotation.ArrayRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.resources.R

val AccountTrendType.labelResId: Int
    @ArrayRes
    get() = R.array.trend_type

val AccountTrendType.label: String
    @Composable
    get() = stringArrayResource(
        R.array.trend_type
    ).get(
        when (this) {
            AccountTrendType.Balance -> 0
            AccountTrendType.Expenses -> 1
            AccountTrendType.Income -> 2
        }
    )