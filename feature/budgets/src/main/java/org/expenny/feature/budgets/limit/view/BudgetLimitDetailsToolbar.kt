package org.expenny.feature.budgets.limit.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetLimitDetailsToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    showDeleteButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
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