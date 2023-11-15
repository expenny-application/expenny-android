package org.expenny.feature.getstarted

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import org.expenny.core.common.ExpennySnackbarManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.theme.ExpennyTheme
import org.expenny.feature.getstarted.navigation.GetStartedNavigator
import org.expenny.feature.getstarted.style.GetStartedScreenTransitionStyle
import org.expenny.feature.getstarted.view.GetStartedContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(style = GetStartedScreenTransitionStyle::class)
@Composable
fun GetStartedScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: GetStartedNavigator,
    currencyUnitResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: GetStartedViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()

    BackHandler(!state.showConfirmationDialog && !state.showAbortDialog) {
        vm.onAction(Action.OnBackClick)
    }

    currencyUnitResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(Action.OnCurrencyUnitSelect(res.value.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is Event.ShowMessage -> snackbarManager.showMessage(it.message)
            is Event.NavigateToApp -> navigator.navigateToHome()
            is Event.NavigateToCurrencyUnitsSelectionList -> navigator.navigateToCurrencyUnitSelectionListScreen(it.selectedId)
            is Event.NavigateBack -> navigator.navigateBack()
        }
    }

    GetStartedContent(
        state = state,
        scrollState = scrollState,
        onAction = vm::onAction,
    )
}

@Preview
@Composable
private fun GetStartedScreenPreview() {
    ExpennyTheme {
        GetStartedContent(
            scrollState = rememberScrollState(),
            state = State(
                showSetupCashBalanceInputFields = true,
                showSetupCashBalanceCheckBox = true,
            ),
            onAction = {},
        )
    }
}
