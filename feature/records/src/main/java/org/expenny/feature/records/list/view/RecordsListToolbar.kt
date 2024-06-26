package org.expenny.feature.records.list.view

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsListToolbar(
    isSelectionMode: Boolean,
    selectionCount: Int,
    drawerState: ExpennyDrawerManager,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    onCloseSelectionMode: () -> Unit,
    onSelectAllClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    BackHandler(isSelectionMode) {
        onCloseSelectionMode()
    }

    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        title = {
            ToolbarTitle(
                text = if (isSelectionMode) {
                    stringResource(R.string.selected_label, selectionCount)
                } else {
                    stringResource(R.string.records_label)
                }
            )
        },
        actions = {
            if (isSelectionMode) {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_check),
                    onClick = onSelectAllClick
                )
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_delete),
                    onClick = onDeleteClick
                )
            } else {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_add),
                    onClick = onAddClick
                )
            }
        },
        navigationIcon = {
            if (drawerState.isDrawerTabAsState && !isSelectionMode) {
                drawerState.NavigationDrawerIcon()
            } else {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_back),
                    onClick = {
                        if (isSelectionMode) onCloseSelectionMode()
                        else onBackClick()
                    }
                )
            }
        }
    )
}
