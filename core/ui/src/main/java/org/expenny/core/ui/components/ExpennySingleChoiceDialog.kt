package org.expenny.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.drawVerticalScrollbar
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyRadioButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySingleChoiceDialog(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selectedIndex: Int,
    onItemSelect: (index: Int) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            title = title,
            items = items,
            selectedIndex = selectedIndex,
            onItemSelect = onItemSelect,
            onCancelClick = onCancelClick
        )
    }
}

@Composable
private fun DialogContent(
    title: String,
    items: List<String>,
    selectedIndex: Int,
    onItemSelect: (index: Int) -> Unit,
    onCancelClick: () -> Unit
) {
    Surface(
        modifier = Modifier.wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(all = 24.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DialogHeader(
                title = title
            )
            DialogList(
                modifier = Modifier.wrapContentHeight(),
                selectedIndex = selectedIndex,
                items = items,
                onClick = {
                    onItemSelect(it)
                }
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.End)
            ) {
                ExpennyButton(
                    onClick = onCancelClick,
                    attributes = ExpennyFlatButtonAttributes(
                        type = ExpennyFlatButtonType.Tertiary,
                        size = ExpennyFlatButtonSize.Medium,
                        label = stringResource(R.string.cancel_button)
                    )
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier.height(64.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        ExpennyText(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun DialogList(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIndex: Int,
    onClick: (index: Int) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.drawVerticalScrollbar(listState),
        state = listState
    ) {
        itemsIndexed(items = items) { index, item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick(index) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExpennyRadioButton(
                    isSelected = index == selectedIndex,
                    onClick = { onClick(index) }
                )
                ExpennyText(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}