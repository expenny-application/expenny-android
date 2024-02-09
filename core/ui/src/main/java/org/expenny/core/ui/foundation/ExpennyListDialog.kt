package org.expenny.core.ui.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.extensions.drawVerticalScrollbar

@Composable
fun ExpennyListDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    listContent: LazyListScope.() -> Unit,
    dialogProperties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val maxDialogHeightPx = LocalDensity.current.run { (configuration.screenHeightDp.dp.toPx() * 0.5).toInt() }
    val maxDialogHeightDp = LocalDensity.current.run { maxDialogHeightPx.toDp() }

    ExpennyDialog(
        modifier = modifier,
        title = title,
        icon = null,
        content = {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = maxDialogHeightDp)
                    .drawVerticalScrollbar(lazyListState),
                state = lazyListState,
            ) {
                listContent()
            }
        },
        dismissButton = dismissButton,
        confirmButton = confirmButton,
        dialogProperties = dialogProperties,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun ExpennyListDialogItem(
    modifier: Modifier = Modifier,
    label: String,
    selectionType: SelectionType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ExpennySelectionButton(
            type = selectionType,
            isSelected = isSelected,
            onClick = { onClick() }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}