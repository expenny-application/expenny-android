package org.expenny.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.models.StringResource

@Composable
fun StringResource.asRawString() = when (this) {
    is StringResource.SimpleStringResource -> str
    is StringResource.IdStringResource -> stringResource(id, *args)
    is StringResource.IdArrayStringResource -> stringArrayResource(id)[index]
    is StringResource.IdQuantityStringResource -> pluralStringResource(pluralId, quantity)
}