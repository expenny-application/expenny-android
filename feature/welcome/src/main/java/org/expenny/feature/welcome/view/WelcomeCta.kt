package org.expenny.feature.welcome.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyButton

@Composable
internal fun WelcomeCta(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ButtonLabel(text = stringResource(R.string.get_started_button))
    }
}