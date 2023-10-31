package org.expenny.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.feature.settings.navigation.SettingsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
fun SettingsScreen(
    navigator: SettingsNavigator,
    snackbarManager: ExpennySnackbarManager,
) {
    val vm: SettingsViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            Event.NavigateBack -> {
                navigator.navigateBack()
            }
            Event.NavigateToLabels -> {
                navigator.navigateToLabelsListScreen()
            }
            Event.NavigateToCurrencies -> {
                navigator.navigateToCurrenciesListScreen()
            }
            else -> {}
        }
    }

    SettingsContent(
        state = state,
        onAction = vm::onAction
    )
}