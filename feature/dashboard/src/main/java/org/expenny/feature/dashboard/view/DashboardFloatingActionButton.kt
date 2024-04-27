package org.expenny.feature.dashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab

@Composable
internal fun DashboardFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyFab(
        modifier = modifier,
        onClick = onClick,
        content = {
            FabIcon(painter = painterResource(R.drawable.ic_bolt))
        }
    )
}
