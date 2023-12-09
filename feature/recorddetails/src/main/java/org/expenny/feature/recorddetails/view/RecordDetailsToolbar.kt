package org.expenny.feature.recorddetails.view

import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsToolbar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    showDeleteButton: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ExpennyToolbar(
        backgroundColor = MaterialTheme.colorScheme.surface,
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
                    painter = painterResource(R.drawable.ic_back),
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