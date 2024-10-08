package org.expenny.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.NavigationBarBottomPadding
import kotlin.reflect.KProperty1

@Composable
fun <B> ExpennyVerticalList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    list: List<B>,
    listItemKey: KProperty1<B, Any>? = null,
    headerItem: @Composable (LazyItemScope.() -> Unit)? = null,
    footerItem: @Composable (LazyItemScope.() -> Unit)? = null,
    listItem: @Composable LazyItemScope.(B) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = state,
        contentPadding = ExpennyVerticalListPaddingValues
    ) {
        headerItem?.let {
            item {
                it()
            }
        }
        items(
            items = list,
            key = listItemKey?.let { { listItemKey.get(it) } } ?: { it.toString() }
        ) {
            listItem(it)
        }
        footerItem?.let {
            item {
                it()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <A, B> ExpennyGroupedVerticalList(
    modifier: Modifier = Modifier,
    groupedList: Map<A, List<B>>,
    state: LazyListState = rememberLazyListState(),
    listItemKey: KProperty1<B, Any>? = null,
    listItem: @Composable LazyItemScope.(B) -> Unit,
    listItemHeader: @Composable LazyItemScope.(A) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = ExpennyVerticalListPaddingValues
    ) {
        groupedList.forEach { (header, list) ->
            stickyHeader(key = header) {
                listItemHeader(header)
            }

            items(
                items = list,
                key = listItemKey?.let { { listItemKey.get(it) } }
            ) {
                listItem(it)
            }
        }
    }
}

val ExpennyVerticalListPaddingValues
    @Composable get() = PaddingValues(
        top = 16.dp,
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp + NavigationBarBottomPadding
    )
