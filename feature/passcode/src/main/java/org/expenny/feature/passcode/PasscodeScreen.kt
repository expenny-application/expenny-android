package org.expenny.feature.passcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.feature.passcode.navigation.PasscodeNavArgs
import org.expenny.feature.passcode.navigation.PasscodeNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = PasscodeNavArgs::class)
@Composable
fun PasscodeScreen(
    navigator: PasscodeNavigator,
    snackbarManager: ExpennySnackbarManager,
) {
    val vm: PasscodeViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.NavigateToDashboard -> {
                navigator.navigateToDashboardScreen()
            }
            is Event.ShowMessage -> {
                snackbarManager.showMessage(it.message)
            }
        }
    }

    PasscodeContent(
        state = state,
        onAction = vm::onAction
    )
}