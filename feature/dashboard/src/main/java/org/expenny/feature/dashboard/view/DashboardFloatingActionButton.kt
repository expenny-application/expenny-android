package org.expenny.feature.dashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonType


@Composable
internal fun DashboardFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onClick,
        attributes = ExpennyFloatingButtonAttributes(
            type = ExpennyFloatingButtonType.Primary,
            size = ExpennyFloatingButtonSize.Large,
            icon = painterResource(R.drawable.ic_bolt)
        )
    )
}
