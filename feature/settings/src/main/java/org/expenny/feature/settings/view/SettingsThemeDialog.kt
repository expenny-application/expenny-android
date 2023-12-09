package org.expenny.feature.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.drawVerticalScrollbar
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonSize
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyRadioButton
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun SettingsThemeDialog(
    modifier: Modifier = Modifier,
    selectedTheme: ApplicationTheme,
    onThemeSelect: (ApplicationTheme) -> Unit,
    onDismiss: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    ExpennyAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.select_theme_label))
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .drawVerticalScrollbar(lazyListState),
                state = lazyListState,
            ) {
                ApplicationTheme.values().forEach {
                    item {
                        DialogListItem(
                            label = it.label,
                            isSelected = it == selectedTheme,
                            onClick = {
                                onThemeSelect(it)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                size = ExpennyButtonSize.Small,
                onClick = onDismiss,
                label = {
                    ExpennyText(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}

@Composable
private fun LazyItemScope.DialogListItem(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExpennyRadioButton(
            isSelected = isSelected,
            onClick = onClick
        )
        ExpennyText(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}