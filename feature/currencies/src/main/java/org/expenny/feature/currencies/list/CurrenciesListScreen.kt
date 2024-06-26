package org.expenny.feature.currencies.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.currencies.list.contract.CurrenciesListEvent
import org.expenny.feature.currencies.list.navigation.CurrenciesListNavArgs
import org.expenny.feature.currencies.list.navigation.CurrenciesListNavigator
import org.expenny.feature.currencies.list.view.CurrenciesListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = CurrenciesListNavArgs::class)
@Composable
fun CurrenciesListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: CurrenciesListNavigator,
    resultNavigator: ResultBackNavigator<LongNavArg>
) {
    val vm: CurrenciesListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is CurrenciesListEvent.ShowMessage -> snackbarManager.showInfo(it.message)
            is CurrenciesListEvent.NavigateBackWithResult -> resultNavigator.navigateBack(it.selection)
            is CurrenciesListEvent.NavigateBack -> navigator.navigateBack()
            is CurrenciesListEvent.NavigateToCreateCurrency -> navigator.navigateToCreateCurrencyScreen()
            is CurrenciesListEvent.NavigateToEditCurrency -> navigator.navigateToEditCurrencyScreen(it.id)
        }
    }

    CurrenciesListContent(
        state = state,
        onAction = vm::onAction
    )
}

