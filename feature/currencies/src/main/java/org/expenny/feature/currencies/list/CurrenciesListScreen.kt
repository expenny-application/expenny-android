package org.expenny.feature.currencies.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.CurrencyUi
import org.expenny.core.ui.foundation.ExpennyTheme
import org.expenny.feature.currencies.list.contract.CurrenciesListEvent
import org.expenny.feature.currencies.list.contract.CurrenciesListState
import org.expenny.feature.currencies.list.navigation.CurrenciesListNavArgs
import org.expenny.feature.currencies.list.navigation.CurrenciesListNavigator
import org.expenny.feature.currencies.list.view.CurrenciesListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.math.BigDecimal

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

@Preview
@Composable
private fun CurrenciesListScreenPreview() {
    ExpennyTheme {
        CurrenciesListContent(
            state = CurrenciesListState(
                currencies = listOf(
                    CurrencyUi(
                        id = 1,
                        code = "EUR",
                        name = "Euro",
                        rate = AmountUi(
                            value = BigDecimal.ONE,
                            displayValue = "1"
                        ),
                        preview = "",
                        isMain = true,
                    )
                )
            ),
            onAction = {},
        )
    }
}
