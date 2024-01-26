package org.expenny.feature.welcome.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@Composable
internal fun WelcomeCta(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFlatButtonAttributes(
            type = ExpennyFlatButtonType.Primary,
            size = ExpennyFlatButtonSize.Large,
            label = stringResource(R.string.get_started_button)
        )
    )
}