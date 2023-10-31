package org.expenny.feature.currencyunits

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.foundation.transitions.VerticalSlideTransitionStyle
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.theme.ExpennyTheme
import org.expenny.feature.currencyunits.navigation.CurrencyUnitsListNavArgs
import org.expenny.feature.currencyunits.view.CurrencyUnitsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(
    navArgsDelegate = CurrencyUnitsListNavArgs::class,
    style = VerticalSlideTransitionStyle::class
)
@Composable
fun CurrencyUnitsListScreen(
    snackbarManager: ExpennySnackbarManager,
    resultNavigator: ResultBackNavigator<LongNavArg>
) {
    val vm: CurrencyUnitsListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBackWithResult -> resultNavigator.navigateBack(it.result)
            is Event.NavigateBack -> resultNavigator.navigateBack()
        }
    }

    CurrencyUnitsListContent(
        state = state,
        onAction = vm::onAction,
    )
}

@Preview
@Composable
private fun CurrencyUnitsListScreenPreview() {
    ExpennyTheme {
        CurrencyUnitsListContent(
            state = State(),
            onAction = {},
        )
    }
}
