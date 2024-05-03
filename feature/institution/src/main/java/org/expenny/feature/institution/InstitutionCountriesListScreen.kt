package org.expenny.feature.institution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.institution.contract.InstitutionCountriesListEvent
import org.expenny.feature.institution.navigation.InstitutionCountriesListNavigator
import org.expenny.feature.institution.view.InstitutionCountriesListContent
import org.expenny.feature.institution.viewmodel.InstitutionCountriesListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
fun InstitutionCountriesListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: InstitutionCountriesListNavigator
) {
    val vm: InstitutionCountriesListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is InstitutionCountriesListEvent.NavigateToInstitutionsList ->
                navigator.navigateToInstitutionsListScreen(it.countryCode)
            is InstitutionCountriesListEvent.ShowError ->
                snackbarManager.showError(it.message)
            is InstitutionCountriesListEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    InstitutionCountriesListContent(
        state = state,
        onAction = vm::onAction,
    )
}