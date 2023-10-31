package org.expenny.feature.getstarted.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.foundation.ExpennyToolbar
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GetStartedToolbar(
    onBackClick: () -> Unit
) {
    ExpennyToolbar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        title = {}
    )
}