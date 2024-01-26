package org.expenny.feature.getstarted.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType


@Composable
internal fun GetStartedCta(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFlatButtonAttributes(
            isEnabled = isEnabled,
            type = ExpennyFlatButtonType.Primary,
            size = ExpennyFlatButtonSize.Large,
            label = stringResource(R.string.continue_button)
        )
    )
}
