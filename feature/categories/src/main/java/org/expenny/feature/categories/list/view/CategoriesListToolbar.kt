package org.expenny.feature.categories.list.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoriesListToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
            ToolbarIcon(
                onClick = onAddClick,
                painter = painterResource(R.drawable.ic_add)
            )
        },
        navigationIcon = {
            ToolbarIcon(
                onClick = onBackClick,
                painter = painterResource(R.drawable.ic_back)
            )
        },
        title = {
            ToolbarTitle(text = title)
        }
    )
}