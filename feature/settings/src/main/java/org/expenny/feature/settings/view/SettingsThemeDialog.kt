package org.expenny.feature.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.drawVerticalScrollbar
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun SettingsThemeDialog(
    modifier: Modifier = Modifier,
    selectedTheme: ApplicationTheme,
    onThemeSelect: (ApplicationTheme) -> Unit,
    onDismiss: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_theme_label))
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
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogListItem(
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
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}