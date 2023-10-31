package org.expenny.feature.currencyunits.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennySearchToolbar
import org.expenny.core.ui.foundation.ExpennyText


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
            ExpennyText(
                text = stringResource(R.string.select_currency_code_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        searchQuery = searchQuery,
        onQueryChange = onSearchQueryChange,
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
    )
}