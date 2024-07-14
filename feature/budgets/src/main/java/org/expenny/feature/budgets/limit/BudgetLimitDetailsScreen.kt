package org.expenny.feature.budgets.limit

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.budgets.limit.navigation.BudgetLimitDetailsNavArgs
import org.expenny.feature.budgets.limit.navigation.BudgetLimitDetailsNavigator
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsAction
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsEvent
import org.expenny.feature.budgets.limit.view.BudgetLimitDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = BudgetLimitDetailsNavArgs::class)
@Composable
fun BudgetLimitDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: BudgetLimitDetailsNavigator,
    categoryResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: BudgetLimitDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val limitInputFocusRequester = remember { FocusRequester() }

    categoryResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(BudgetLimitDetailsAction.OnCategorySelect(res.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is BudgetLimitDetailsEvent.ShowMessage -> {
                snackbarManager.showInfo(it.message)
            }
            is BudgetLimitDetailsEvent.ShowError -> {
                snackbarManager.showError(it.message)
            }
            is BudgetLimitDetailsEvent.NavigateBack -> {
                navigator.navigateBack()
            }
            is BudgetLimitDetailsEvent.NavigateToCategorySelectionList -> {
                navigator.navigateToCategorySelectionListScreen(it.selection, it.excludeIds)
            }
            is BudgetLimitDetailsEvent.RequestLimitInputFocus -> {
                limitInputFocusRequester.requestFocus()
            }
        }
    }

    BudgetLimitDetailsContent(
        state = state,
        scrollState = scrollState,
        limitInputFocusRequester = limitInputFocusRequester,
        onAction = vm::onAction
    )
}