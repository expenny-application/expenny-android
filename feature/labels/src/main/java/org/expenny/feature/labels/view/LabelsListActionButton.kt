package org.expenny.feature.labels.view

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFloatingActionButton
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun LabelsListActionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExpennyFloatingActionButton(
        modifier = modifier,
        isExpanded = isExpanded,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_done),
                contentDescription = null
            )
        },
        text = {
            ExpennyText(text = stringResource(R.string.confirm_button))
        }
    )
}