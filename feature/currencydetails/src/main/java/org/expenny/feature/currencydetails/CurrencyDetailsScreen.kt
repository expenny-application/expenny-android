package org.expenny.feature.currencydetails

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.currencydetails.navigation.CurrencyDetailsNavArgs
import org.expenny.feature.currencydetails.navigation.CurrencyDetailsNavigator
import org.expenny.feature.currencydetails.view.CurrencyDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = CurrencyDetailsNavArgs::class)
@Composable
fun CurrencyDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: CurrencyDetailsNavigator,
    currencyUnitResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: CurrencyDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()

    currencyUnitResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(Action.OnCurrencyUnitSelect(res.value.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateToCurrencyUnitsSelectionList -> navigator.navigateToCurrencyUnitSelectionListScreen(it.selectedId)
            is Event.ShowMessage -> snackbarManager.showMessage(it.message)
            is Event.NavigateBack -> navigator.navigateBack()
        }
    }

    CurrencyDetailsContent(
        state = state,
        scrollState = scrollState,
        onAction = vm::onAction
    )
}