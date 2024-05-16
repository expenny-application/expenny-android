package org.expenny.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.NavigationBarBottomPadding
import kotlin.reflect.KProperty1

@Composable
fun <B> ExpennyVerticalGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    columns: GridCells,
    list: List<B>,
    listItemKey: KProperty1<B, Any>? = null,
    listItem: @Composable LazyGridItemScope.(B) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = state,
        columns = columns,
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp + NavigationBarBottomPadding
        )
    ) {
        items(
            items = list,
            key = listItemKey?.let { { listItemKey.get(it) } }
        ) {
            listItem(it)
        }
    }
}