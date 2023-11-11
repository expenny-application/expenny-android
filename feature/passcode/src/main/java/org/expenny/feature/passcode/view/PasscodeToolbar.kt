package org.expenny.feature.passcode.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasscodeToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    ExpennyToolbar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        title = {
            ExpennyText(
                text = stringResource(R.string.set_passcode_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}