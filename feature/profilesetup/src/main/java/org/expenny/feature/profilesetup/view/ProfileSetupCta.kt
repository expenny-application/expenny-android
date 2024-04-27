package org.expenny.feature.profilesetup.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyButton

@Composable
internal fun ProfileSetupCta(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        isEnabled = isEnabled,
        label = {
            ButtonLabel(text = stringResource(R.string.continue_button))
        }
    )
}
