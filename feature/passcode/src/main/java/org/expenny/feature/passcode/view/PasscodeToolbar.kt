package org.expenny.feature.passcode.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasscodeToolbar(
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onInfoClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.set_passcode_label))
        }
    )
}