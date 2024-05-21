package org.expenny.feature.records.details.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R

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
    TopAppBar(
        actions = {
            if (showInfoButton) {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null
                    )
                }
            }
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
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = title)
        }
    )
}