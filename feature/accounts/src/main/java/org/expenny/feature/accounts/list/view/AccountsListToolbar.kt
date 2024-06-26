package org.expenny.feature.accounts.list.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsListToolbar(
    drawerState: ExpennyDrawerManager,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
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
            if (drawerState.isDrawerTabAsState) {
                drawerState.NavigationDrawerIcon()
            } else {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_back),
                    onClick = onBackClick
                )
            }
        },
        title = {
            ToolbarTitle(text = title)
        }
    )
}
