package org.expenny.feature.currencyunits.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySearchToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyUnitsListToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    ExpennySearchToolbar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = stringResource(R.string.select_currency_code_label))
        },
        searchQuery = searchQuery,
        onQueryChange = onSearchQueryChange,
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