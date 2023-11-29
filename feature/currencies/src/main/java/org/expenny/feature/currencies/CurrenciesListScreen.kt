package org.expenny.feature.currencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.ui.AmountUi
import org.expenny.core.ui.data.ui.CurrencyUi
import org.expenny.core.ui.theme.ExpennyTheme
import org.expenny.feature.currencies.navigation.CurrenciesListNavArgs
import org.expenny.feature.currencies.navigation.CurrenciesListNavigator
import org.expenny.feature.currencies.view.CurrenciesListContent
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
            is Event.ShowMessage -> snackbarManager.showMessage(it.message)
            is Event.NavigateBackWithResult -> resultNavigator.navigateBack(it.selection)
            is Event.NavigateBack -> navigator.navigateBack()
            is Event.NavigateToCreateCurrency -> navigator.navigateToCreateCurrencyScreen()
            is Event.NavigateToEditCurrency -> navigator.navigateToEditCurrencyScreen(it.id)
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
            state = State(
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
