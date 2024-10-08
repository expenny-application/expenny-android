package org.expenny.feature.institution.requisition

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionAction
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionEvent
import org.expenny.feature.institution.requisition.navigation.InstitutionRequisitionNavArgs
import org.expenny.feature.institution.requisition.navigation.InstitutionRequisitionNavigator
import org.expenny.feature.institution.requisition.view.InstitutionRequisitionContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = InstitutionRequisitionNavArgs::class)
@Composable
fun InstitutionRequisitionScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: InstitutionRequisitionNavigator
) {
    val vm: InstitutionRequisitionViewModel = hiltViewModel()
    val state by vm.collectAsState()

    BackHandler {
        vm.onAction(InstitutionRequisitionAction.OnRequisitionAborted)
    }

    vm.collectSideEffect {
        when (it) {
            is InstitutionRequisitionEvent.NavigateToBackToAccountsList ->
                navigator.navigateBackToAccountsListScreen()
            is InstitutionRequisitionEvent.ShowError ->
                snackbarManager.showError(it.message)
            is InstitutionRequisitionEvent.ShowMessage ->
                snackbarManager.showInfo(it.message)
            is InstitutionRequisitionEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    InstitutionRequisitionContent(
        state = state,
        onAction = vm::onAction,
    )
}