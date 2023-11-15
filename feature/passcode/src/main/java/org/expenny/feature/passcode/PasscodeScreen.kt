package org.expenny.feature.passcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.components.rememberBiometricPromptState
import org.expenny.core.ui.utils.OnLifecycleEvent
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
                snackbarManager.showMessage(it.message)
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