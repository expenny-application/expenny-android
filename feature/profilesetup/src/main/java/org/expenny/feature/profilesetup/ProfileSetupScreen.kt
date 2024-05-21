package org.expenny.feature.profilesetup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.profilesetup.contract.ProfileSetupAction
import org.expenny.feature.profilesetup.contract.ProfileSetupEvent
import org.expenny.feature.profilesetup.navigation.ProfileSetupNavigator
import org.expenny.feature.profilesetup.style.ProfileSetupScreenTransitionStyle
import org.expenny.feature.profilesetup.view.ProfileSetupContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(style = ProfileSetupScreenTransitionStyle::class)
@Composable
fun ProfileSetupScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: ProfileSetupNavigator,
    currencyUnitResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: ProfileSetupViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()

    BackHandler(!state.showConfirmationDialog && !state.showAbortDialog) {
        vm.onAction(ProfileSetupAction.OnBackClick)
    }

    currencyUnitResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(ProfileSetupAction.OnCurrencyUnitSelect(res.value.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is ProfileSetupEvent.ShowMessage -> snackbarManager.showInfo(it.message)
            is ProfileSetupEvent.NavigateToHome -> navigator.navigateToHome()
            is ProfileSetupEvent.NavigateToCurrencyUnitsSelectionList -> navigator.navigateToCurrencyUnitSelectionListScreen(it.selectedId)
            is ProfileSetupEvent.NavigateBack -> navigator.navigateBack()
        }
    }

    ProfileSetupContent(
        state = state,
        scrollState = scrollState,
        onAction = vm::onAction,
    )
}
