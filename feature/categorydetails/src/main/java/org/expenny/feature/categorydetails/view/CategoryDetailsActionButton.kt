package org.expenny.feature.categorydetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonType

@Composable
internal fun AccountDetailsActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFloatingButtonAttributes(
            isExpanded = true,
            type = ExpennyFloatingButtonType.Primary,
            size = ExpennyFloatingButtonSize.Large,
            label = stringResource(R.string.save_button),
            icon = painterResource(R.drawable.ic_check)
        )
    )
}