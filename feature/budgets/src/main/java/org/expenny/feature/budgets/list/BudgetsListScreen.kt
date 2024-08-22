package org.expenny.feature.budgets.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.budgets.list.contract.BudgetsListEvent
import org.expenny.feature.budgets.list.navigation.BudgetsListNavigator
import org.expenny.feature.budgets.list.view.BudgetsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
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
    val actionsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    vm.collectSideEffect {
        when (it) {
            is BudgetsListEvent.NavigateToBudgetOverview -> navigator.navigateToBudgetOverviewScreen(it.id, it.intervalType)
            is BudgetsListEvent.NavigateToOnetimeBudgetCreate -> navigator.navigateToCreateOnetimeBudgetScreen()
            is BudgetsListEvent.ShowError -> snackbarManager.showError(it.error)
            is BudgetsListEvent.ShowMessage -> snackbarManager.showInfo(it.message)
            is BudgetsListEvent.NavigateBack -> navigator.navigateBack()
        }
    }

    BudgetsListContent(
        state = state,
        coroutineScope = scope,
        actionsSheetState = actionsSheetState,
        periodicBudgetsLazyListState = periodicBudgetsLazyListState,
        onetimeBudgetsLazyListState = onetimeBudgetsLazyListState,
        drawerState = drawerState,
        onAction = vm::onAction
    )
}