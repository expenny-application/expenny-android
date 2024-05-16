package org.expenny.feature.institution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewEvent
import org.expenny.feature.institution.navigation.InstitutionAccountsPreviewNavArgs
import org.expenny.feature.institution.navigation.InstitutionAccountsPreviewNavigator
import org.expenny.feature.institution.view.InstitutionAccountsPreviewContent
import org.expenny.feature.institution.viewmodel.InstitutionAccountsPreviewViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = InstitutionAccountsPreviewNavArgs::class)
@Composable
fun InstitutionAccountsPreviewScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: InstitutionAccountsPreviewNavigator
) {
    val vm: InstitutionAccountsPreviewViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is InstitutionAccountsPreviewEvent.NavigateToBackToAccountsList ->
                navigator.navigateBackToAccountsListScreen()
            is InstitutionAccountsPreviewEvent.ShowError -> {
                snackbarManager.showError(it.message)
            }
        }
    }

    InstitutionAccountsPreviewContent(
        state = state,
        onAction = vm::onAction
    )
}