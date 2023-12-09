package org.expenny.feature.currencies.view

import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrenciesListToolbar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    ExpennyToolbar(
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