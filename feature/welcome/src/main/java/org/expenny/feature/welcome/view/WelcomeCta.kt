package org.expenny.feature.welcome.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFlatButton

@Composable
internal fun WelcomeCta(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyFlatButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = stringResource(R.string.get_started_button))
    }
}