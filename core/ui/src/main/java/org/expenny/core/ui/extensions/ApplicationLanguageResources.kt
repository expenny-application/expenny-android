package org.expenny.core.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.resources.R

val ApplicationLanguage.labelResId: Int
    @StringRes
    get() = when (this) {
        ApplicationLanguage.SystemDefault -> R.string.system_default_label
        ApplicationLanguage.English -> R.string.en_locale
        ApplicationLanguage.Belarusian -> R.string.be_locale
        ApplicationLanguage.Russian -> R.string.ru_locale
    }

val ApplicationLanguage.label: String
    @Composable
    get() = stringResource(labelResId)

