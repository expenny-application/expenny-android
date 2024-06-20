package org.expenny.feature.records.labels.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySearchTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyUnitsListToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    showSearch: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    ExpennySearchTopBar(
        scrollBehavior = scrollBehavior,
        isSearchShown = showSearch,
        searchQuery = searchQuery,
        onQueryChange = onSearchQueryChange,
        title = {
            Text(text = stringResource(R.string.select_labels_label))
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null
                )
            }
        },
    )
}