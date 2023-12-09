package org.expenny.core.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.resources.R

val ApplicationTheme.labelResId: Int
    @StringRes
    get() = when (this) {
        ApplicationTheme.SystemDefault -> R.string.system_default_label
        ApplicationTheme.Dark -> R.string.dark_label
        ApplicationTheme.Light -> R.string.light_label
    }

val ApplicationTheme.label: String
    @Composable
    get() = stringResource(labelResId)