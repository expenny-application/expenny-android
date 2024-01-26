package org.expenny.feature.daterangepicker.view

import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateRangePickerToolbar(
    showClearButton: Boolean,
    onClearClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    ExpennyToolbar(
        actions = {
            if (showClearButton) {
                ExpennyButton(
                    onClick = onClearClick,
                    attributes = ExpennyFlatButtonAttributes(
                        type = ExpennyFlatButtonType.Tertiary,
                        size = ExpennyFlatButtonSize.Small,
                        label = stringResource(R.string.clear_button)
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        title = {
            ExpennyText(
                text = stringResource(R.string.select_range_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}