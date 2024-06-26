package org.expenny.feature.categories.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.feature.categories.details.contract.CategoryDetailsState

@Composable
internal fun CategoryDetailsInputForm(
    modifier: Modifier = Modifier,
    state: CategoryDetailsState,
    scrollState: ScrollState,
    nameInputFocusRequester: FocusRequester,
    onNameChange: (String) -> Unit,
    onColorChange: (Color) -> Unit,
    onIconChange: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .imePadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 56.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameInputField(
            modifier = Modifier.focusRequester(nameInputFocusRequester),
            iconResId = state.selectedIconResId,
            iconColor = state.selectedColor,
            state = state.nameInput,
            onValueChange = onNameChange
        )
        ExpennySection(title = stringResource(R.string.color_label)) {
            ColorPickerRow(
                colors = state.colors,
                selectedColor = state.selectedColor,
                onColorSelect = onColorChange
            )
        }
        ExpennySection(title = stringResource(R.string.icon_label)) {
            IconPickerRow(
                icons = state.icons,
                selectedIconResId = state.selectedIconResId,
                onIconSelect = onIconChange
            )
        }
    }
}

@Composable
private fun NameInputField(
    modifier: Modifier = Modifier,
    iconResId: Int?,
    iconColor: Color?,
    state: InputUi,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier.fillMaxWidth(),
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.name_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            leadingContent = {
                if (iconResId != null && iconColor != null) {
                    Icon(
                        painter = painterResource(iconResId),
                        tint = iconColor,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
private fun IconPickerRow(
    modifier: Modifier = Modifier,
    icons: List<Int>,
    selectedIconResId: Int,
    onIconSelect: (Int) -> Unit
) {
    ItemPicker(
        modifier = modifier,
        items = icons,
        selectedItem = selectedIconResId,
    ) { item, isSelected ->
        ItemBox(
            isSelected = isSelected,
            painter = painterResource(item),
            color = MaterialTheme.colorScheme.onSurface,
            onClick = { onIconSelect(item) }
        )
    }
}

@Composable
private fun ColorPickerRow(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: Color,
    onColorSelect: (Color) -> Unit
) {
    ItemPicker(
        modifier = modifier,
        items = colors,
        selectedItem = selectedColor,
    ) {item, isSelected ->
        ItemBox(
            isSelected = isSelected,
            painter = painterResource(
                if (isSelected) R.drawable.ic_check_circle
                else R.drawable.ic_filled_circle
            ),
            color = item,
            onClick = { onColorSelect(item) }
        )
    }
}

@Composable
private fun <T> ItemPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    itemContent: @Composable LazyItemScope.(item: T, isSelected: Boolean) -> Unit,
) {
    val state = rememberLazyListState()

    LaunchedEffect(selectedItem) {
        if (selectedItem != null) {
            items.indexOfFirst { it == selectedItem }
                .let { if (it == -1) 0 else it }
                .also { state.animateScrollToItem(it) }
        }
    }

    LazyRow(
        modifier = modifier,
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(items = items) {
            itemContent(
                item = it,
                isSelected = it == selectedItem
            )
        }
    }
}

@Composable
private fun ItemBox(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    painter: Painter,
    color: Color,
    onClick: () -> Unit,
) {
    val boxColor =
        if (isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
        else MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(boxColor)
            .clickable { onClick() }
            .size(40.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painter,
            tint = color,
            contentDescription = null,
        )
    }
}
