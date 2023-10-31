package org.expenny.core.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.SortType
import org.expenny.core.resources.R

val SortType.labelResId: Int
    @StringRes
    get() = when (this) {
        SortType.Date -> R.string.sort_by_date_label
        SortType.HighestAmount -> R.string.sort_by_highest_amount_label
        SortType.LowestAmount -> R.string.sort_by_lowest_amount_label
    }

val SortType.label: String
    @Composable
    get() = stringResource(labelResId)