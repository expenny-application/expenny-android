package org.expenny.feature.dashboard.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFloatingActionButton


@Composable
internal fun DashboardFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_bolt),
                contentDescription = null
            )
        }
    )
}
