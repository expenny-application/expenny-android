package org.expenny.feature.accounts.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFloatingButton

@Composable
internal fun AccountsListConfirmSelectionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExpennyFloatingButton(
        modifier = modifier,
        onClick = onClick,
        isExpanded = isExpanded,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null
            )
        },
        label = {
            Text(text = stringResource(R.string.confirm_button))
        }
    )
}