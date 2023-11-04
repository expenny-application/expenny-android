package org.expenny.feature.getstarted.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText


@Composable
internal fun GetStartedCta(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        isEnabled = enabled,
        onClick = onClick,
        label = {
            ExpennyText(text = stringResource(R.string.continue_button))
        }
    )
}
