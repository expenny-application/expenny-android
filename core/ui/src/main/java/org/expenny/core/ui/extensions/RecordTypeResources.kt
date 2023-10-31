package org.expenny.core.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R

val RecordType.labelResId: Int
    @StringRes
    get() = when (this) {
        RecordType.Expense -> R.string.expense_label
        RecordType.Income -> R.string.income_label
        RecordType.Transfer -> R.string.transfer_label
    }

val RecordType.label: String
    @Composable
    get() = stringResource(labelResId)