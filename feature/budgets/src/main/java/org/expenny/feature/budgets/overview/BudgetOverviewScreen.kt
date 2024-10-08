package org.expenny.feature.budgets.overview

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.budgets.overview.contract.BudgetOverviewAction
import org.expenny.feature.budgets.overview.contract.BudgetOverviewEvent
import org.expenny.feature.budgets.overview.navigation.BudgetOverviewNavArgs
import org.expenny.feature.budgets.overview.navigation.BudgetOverviewNavigator
import org.expenny.feature.budgets.overview.view.BudgetOverviewContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = BudgetOverviewNavArgs::class)
@Composable
fun BudgetOverviewScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: BudgetOverviewNavigator,
    currencyResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: BudgetOverviewViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val listState = rememberLazyListState()
    val accountsFilterListState = rememberLazyListState()

    currencyResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(BudgetOverviewAction.OnDisplayCurrencySelect(res.value.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is BudgetOverviewEvent.NavigateToBudgetLimitDetails ->
                navigator.navigateToBudgetLimitDetailsScreen(
                    budgetId = it.budgetId,
                    budgetGroupId = it.budgetGroupId,
                    budgetType = it.budgetType,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    excludeCategoriesIds = it.excludeCategoriesIds
                )
            is BudgetOverviewEvent.NavigateToDisplayCurrencySelection ->
                navigator.navigateToCurrencySelectionListScreen(it.selectedId)
            is BudgetOverviewEvent.ShowError ->
                snackbarManager.showError(it.error)
            is BudgetOverviewEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    BudgetOverviewContent(
        state = state,
        accountsFilterListState = accountsFilterListState,
        listState = listState,
        onAction = vm::onAction
    )
}