package org.expenny.feature.dashboard.view

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFloatingButton


@Composable
internal fun DashboardFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyFloatingButton(
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
