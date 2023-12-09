package org.expenny.feature.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.components.rememberBiometricPromptState
import org.expenny.feature.settings.navigation.SettingsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("InlinedApi")
@Destination
@Composable
fun SettingsScreen(
    navigator: SettingsNavigator,
    snackbarManager: ExpennySnackbarManager,
) {
    val vm: SettingsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val activity = LocalContext.current as Activity

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.NavigateToLabels -> {
                navigator.navigateToLabelsListScreen()
            }
            is Event.NavigateToCategoriesList -> {
                navigator.navigateToCategoriesListScreen()
            }
            is Event.NavigateToCurrencies -> {
                navigator.navigateToCurrenciesListScreen()
            }
            is Event.NavigateToCreatePasscode -> {
                navigator.navigateToCreatePasscodeScreen()
            }
            is Event.NavigateToSystemSecuritySettings -> {
                activity.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            }
            is Event.NavigateToSystemAlarmSettings -> {
                activity.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
            is Event.ShowMessage -> {
                snackbarManager.showMessage(it.message)
            }
        }
    }

    SettingsContent(
        state = state,
        onAction = vm::onAction
    )
}