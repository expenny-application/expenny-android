package org.expenny.feature.accounts.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab

@Composable
internal fun AccountsListConfirmSelectionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExpennyFab(
        modifier = modifier,
        onClick = onClick,
        isExpanded = isExpanded,
        icon = {
            FabIcon(painter = painterResource(R.drawable.ic_check))
        },
        label = {
            FabLabel(text = stringResource(R.string.confirm_button))
        }
    )
}