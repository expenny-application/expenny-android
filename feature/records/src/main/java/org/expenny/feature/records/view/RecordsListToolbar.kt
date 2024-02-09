package org.expenny.feature.records.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.utils.ExpennyDrawerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordsListToolbar(
    isSelectionMode: Boolean,
    selectionCount: Int,
    drawerState: ExpennyDrawerState,
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

    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = if (isSelectionMode) {
                    stringResource(R.string.selected_label, selectionCount)
                } else {
                    stringResource(R.string.records_label)
                }
            )
        },
        actions = {
            if (isSelectionMode) {
                IconButton(onClick = onSelectAllClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = onAddClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            }
        },
        navigationIcon = {
            if (drawerState.isDrawerTabAsState && !isSelectionMode) {
                drawerState.NavigationDrawerIcon()
            } else {
                IconButton(
                    onClick = {
                        if (isSelectionMode) onCloseSelectionMode()
                        else onBackClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null
                    )
                }
            }
        }
    )
}
