package org.expenny.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.base.ExpennySnackbarType
import org.expenny.core.ui.base.ExpennySnackbarVisuals

@Composable
fun ExpennySnackbar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
) {
    val type = (hostState.currentSnackbarData?.visuals as? ExpennySnackbarVisuals)?.type
        ?: ExpennySnackbarType.Info

    val containerColor by rememberUpdatedState(
        if (type == ExpennySnackbarType.Error) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.inverseSurface
    )
    val contentColor by rememberUpdatedState(
        if (type == ExpennySnackbarType.Error) MaterialTheme.colorScheme.onError
        else MaterialTheme.colorScheme.inverseOnSurface
    )

    SnackbarHost(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom),
        hostState = hostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(10.dp),
                snackbarData = data,
                containerColor = containerColor,
                contentColor = contentColor,
                actionContentColor = contentColor,
                dismissActionContentColor = contentColor,
            )
        }
    )
}
