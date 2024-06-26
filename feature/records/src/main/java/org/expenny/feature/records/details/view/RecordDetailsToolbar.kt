package org.expenny.feature.records.details.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsToolbar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    showDeleteButton: Boolean,
    showInfoButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
            if (showInfoButton) {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_info),
                    onClick = onInfoClick
                )
            }
            if (showDeleteButton) {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_delete),
                    onClick = onDeleteClick
                )
            }
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