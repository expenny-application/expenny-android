package org.expenny.feature.categorydetails.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryDetailsToolbar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    showDeleteButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        title = {
            ExpennyText(text = title)
        }
    )
}