package org.expenny.feature.passcode.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasscodeToolbar(
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    ExpennyToolbar(
        modifier = modifier,
        navigationIcon = {
            ToolbarIcon(
                painter = painterResource(R.drawable.ic_back),
                onClick = onBackClick
            )
        },
        actions = {
            ToolbarIcon(
                painter = painterResource(R.drawable.ic_info),
                onClick = onInfoClick
            )
        },
        title = {
            ToolbarTitle(text = stringResource(R.string.set_passcode_label))
        }
    )
}