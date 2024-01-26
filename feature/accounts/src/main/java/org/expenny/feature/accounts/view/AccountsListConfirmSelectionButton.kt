package org.expenny.feature.accounts.view

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
internal fun AccountsListConfirmSelectionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFloatingButtonAttributes(
            isExpanded = isExpanded,
            type = ExpennyFloatingButtonType.Primary,
            size = ExpennyFloatingButtonSize.Large,
            label = stringResource(R.string.confirm_button),
            icon = painterResource(R.drawable.ic_check)
        )
    )
}