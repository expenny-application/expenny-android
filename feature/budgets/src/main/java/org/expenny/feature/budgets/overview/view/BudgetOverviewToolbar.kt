package org.expenny.feature.budgets.overview.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetOverviewToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    displayCurrency: String? = null,
    onDisplayCurrencyClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        actions = {
            displayCurrency?.let {
                ToolbarLabel(
                    text = displayCurrency,
                    onClick = {
                        onDisplayCurrencyClick()
                    }
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