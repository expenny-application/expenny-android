package org.expenny.feature.institution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.institution.contract.InstitutionsListEvent
import org.expenny.feature.institution.navigation.InstitutionsListNavArgs
import org.expenny.feature.institution.navigation.InstitutionsListNavigator
import org.expenny.feature.institution.view.InstitutionsListContent
import org.expenny.feature.institution.viewmodel.InstitutionsListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
fun InstitutionsListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: InstitutionsListNavigator,
) {
    val vm: InstitutionsListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is InstitutionsListEvent.NavigateToInstitutionRequisition ->
                navigator.navigateToInstitutionRequisition(it.institutionId)
            is InstitutionsListEvent.ShowError ->
                snackbarManager.showError(it.message)
            is InstitutionsListEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    InstitutionsListContent(
        state = state,
        onAction = vm::onAction,
    )
}