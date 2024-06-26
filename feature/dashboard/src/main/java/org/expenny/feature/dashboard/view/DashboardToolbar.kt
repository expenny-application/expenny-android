package org.expenny.feature.dashboard.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyToolbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: ExpennyDrawerManager,
    displayCurrency: String? = null,
    onDisplayCurrencyClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        title = {
            ToolbarTitle(text = stringResource(R.string.dashboard_label))
        },
        navigationIcon = {
            drawerState.NavigationDrawerIcon()
        },
        actions = {
            displayCurrency?.let {
                ToolbarLabel(
                    text = displayCurrency,
                    onClick = {
                        onDisplayCurrencyClick()
                    }
                )
            }
        }
    )
}
