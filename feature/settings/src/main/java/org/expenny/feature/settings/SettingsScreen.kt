package org.expenny.feature.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.settings.contract.SettingsEvent
import org.expenny.feature.settings.navigation.SettingsNavigator
import org.expenny.feature.settings.view.SettingsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
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
    val profileActionsSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    vm.collectSideEffect {
        when (it) {
            is SettingsEvent.NavigateBack -> {
                navigator.navigateBack()
            }
            is SettingsEvent.NavigateToCreateProfile -> {
                navigator.navigateToCreateProfileScreen()
            }
            is SettingsEvent.NavigateToCategoriesList -> {
                navigator.navigateToCategoriesListScreen()
            }
            is SettingsEvent.NavigateToCurrencies -> {
                navigator.navigateToCurrenciesListScreen()
            }
            is SettingsEvent.NavigateToCreatePasscode -> {
                navigator.navigateToCreatePasscodeScreen()
            }
            is SettingsEvent.NavigateToSystemSecuritySettings -> {
                activity.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            }
            is SettingsEvent.NavigateToSystemAlarmSettings -> {
                activity.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
            is SettingsEvent.ShowMessage -> {
                snackbarManager.showInfo(it.message)
            }
            is SettingsEvent.RestartApplication -> {
                navigator.restartApplication(it.isDataCleanupRequested)
//                activity.finish()
//                activity.startActivity(activity.intent)
            }
        }
    }

    SettingsContent(
        state = state,
        scope = scope,
        profileActionsSheetState = profileActionsSheetState,
        onAction = vm::onAction
    )
}