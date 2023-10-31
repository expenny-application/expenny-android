package org.expenny.feature.welcome.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText


@Composable
internal fun WelcomeContinueButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        label = {
            ExpennyText(text = stringResource(R.string.continue_button))
        },
        onClick = onClick
    )
}