package org.expenny.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.utils.StringResource

@Composable
fun StringResource.asRawString() : String = when (this) {
    is StringResource.SimpleStringResource -> this.str
    is StringResource.IdStringResource -> stringResource(this.id, *this.args)
    is StringResource.IdArrayResource -> stringArrayResource(this.id)[this.index]
    is StringResource.PluralStringResource -> pluralStringResource(this.pluralId, this.quantity)
}