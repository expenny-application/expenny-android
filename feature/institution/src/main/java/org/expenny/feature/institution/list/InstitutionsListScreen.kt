package org.expenny.feature.institution.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.institution.list.contract.InstitutionsListEvent
import org.expenny.feature.institution.list.navigation.InstitutionsListNavigator
import org.expenny.feature.institution.list.view.InstitutionsListContent
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