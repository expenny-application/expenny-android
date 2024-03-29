package org.expenny.feature.categories.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoriesListToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null
                )
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
            Text(text = title)
        }
    )
}