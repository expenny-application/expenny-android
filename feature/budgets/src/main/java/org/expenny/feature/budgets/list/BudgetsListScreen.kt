package org.expenny.feature.budgets.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.budgets.list.contract.BudgetsListEvent
import org.expenny.feature.budgets.list.navigation.BudgetsListNavigator
import org.expenny.feature.budgets.list.view.BudgetsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
fun BudgetsListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: BudgetsListNavigator,
    drawerState: ExpennyDrawerManager
) {
    val vm: BudgetsListViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val periodicBudgetsLazyListState = rememberLazyListState()
    val onetimeBudgetsLazyListState = rememberLazyListState()

    vm.collectSideEffect {
        when (it) {
            is BudgetsListEvent.NavigateToBudgetOverview -> navigator.navigateToPeriodicBudgetOverviewScreen(it.id, it.intervalType, it.budgetType)
            is BudgetsListEvent.NavigateToOnetimeBudgetCreate -> navigator.navigateToCreateOnetimeBudgetScreen()
            is BudgetsListEvent.ShowError -> snackbarManager.showError(it.error)
            is BudgetsListEvent.NavigateBack -> navigator.navigateBack()
        }
    }

    BudgetsListContent(
        state = state,
        periodicBudgetsLazyListState = periodicBudgetsLazyListState,
        onetimeBudgetsLazyListState = onetimeBudgetsLazyListState,
        drawerState = drawerState,
        onAction = vm::onAction
    )
}