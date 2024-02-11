package org.expenny.feature.profilesetup.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFlatButton


@Composable
internal fun ProfileSetupCta(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    ExpennyFlatButton(
        modifier = modifier,
        onClick = onClick,
        isEnabled = isEnabled,
        label = {
            Text(text = stringResource(R.string.continue_button))
        }
    )
}
