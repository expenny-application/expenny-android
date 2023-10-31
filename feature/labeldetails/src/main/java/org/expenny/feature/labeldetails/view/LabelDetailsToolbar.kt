package org.expenny.feature.labeldetails.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelDetailsToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    showDeleteButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ExpennyToolbar(
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior,
        title = {
            ExpennyText(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}