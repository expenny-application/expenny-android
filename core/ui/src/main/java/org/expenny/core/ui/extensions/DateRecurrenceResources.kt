package org.expenny.core.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.DateRecurrence
import org.expenny.core.resources.R

val DateRecurrence.labelResId: Int
    @StringRes
    get() = when (this) {
        DateRecurrence.Daily -> R.string.daily_label
        DateRecurrence.Weekly -> R.string.weekly_label
        DateRecurrence.Monthly -> R.string.monthly_label
        DateRecurrence.Quarterly -> R.string.quarterly_label
        DateRecurrence.Annually -> R.string.annually_label
    }

val DateRecurrence.label: String
    @Composable
    get() = stringResource(labelResId)