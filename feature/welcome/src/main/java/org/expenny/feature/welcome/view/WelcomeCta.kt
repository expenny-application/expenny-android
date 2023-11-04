package org.expenny.feature.welcome.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText


@Composable
internal fun WelcomeCta(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        label = {
            ExpennyText(text = stringResource(R.string.get_started_button))
        },
        onClick = onClick
    )
}