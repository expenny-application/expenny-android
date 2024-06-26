package org.expenny.feature.currencies.list.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrenciesListToolbar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
            ToolbarIcon(
                painter = painterResource(R.drawable.ic_add),
                onClick = onAddClick
            )
        },
        navigationIcon = {
            ToolbarIcon(
                painter = painterResource(R.drawable.ic_back),
                onClick = onBackClick
            )
        },
        title = {
            ToolbarTitle(text = title)
        }
    )
}