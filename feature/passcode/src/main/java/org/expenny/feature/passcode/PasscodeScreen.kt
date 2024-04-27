package org.expenny.feature.passcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.components.rememberBiometricPromptState
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
    val biometricPromptState = rememberBiometricPromptState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.NavigateToDashboard -> {
                navigator.navigateToDashboardScreen()
            }
            is Event.ShowMessage -> {
                snackbarManager.showInfo(it.message)
            }
            is Event.ShowBiometricPrompt -> {
                biometricPromptState.showPrompt(it.cryptoObject)
            }
        }
    }

    PasscodeContent(
        state = state,
        biometricPromptState = biometricPromptState,
        onAction = vm::onAction
    )
}