package org.expenny.feature.accounts.view

import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.utils.ExpennyDrawerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsListToolbar(
    drawerState: ExpennyDrawerState,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        },
        navigationIcon = {
            if (drawerState.isDrawerTabAsState) {
                drawerState.NavigationDrawerIcon()
            } else {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(text = title)
        }
    )
}
