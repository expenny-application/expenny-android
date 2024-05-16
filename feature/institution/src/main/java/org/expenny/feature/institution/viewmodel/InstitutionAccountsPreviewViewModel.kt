package org.expenny.feature.institution.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.usecase.institution.GetInstitutionAccountsUseCase
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewAction
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewEvent
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewState
import org.expenny.feature.institution.navArgs
import org.expenny.feature.institution.navigation.InstitutionAccountsPreviewNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InstitutionAccountsPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInstitutionAccounts: GetInstitutionAccountsUseCase,
) : ExpennyViewModel<InstitutionAccountsPreviewAction>(),
    ContainerHost<InstitutionAccountsPreviewState, InstitutionAccountsPreviewEvent> {

    private val requisitionId = savedStateHandle.navArgs<InstitutionAccountsPreviewNavArgs>().requisitionId

    override val container = container<InstitutionAccountsPreviewState, InstitutionAccountsPreviewEvent>(
        initialState = InstitutionAccountsPreviewState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToAccounts() }
        }
    }

    private fun subscribeToAccounts() = intent {
        getInstitutionAccounts(GetInstitutionAccountsUseCase.Params(requisitionId)).collect {
            when (it) {
                is RemoteResult.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is RemoteResult.Success -> {
                    reduce {
                        state.copy(
                            isLoading = false
                        )
                    }
                }
                is RemoteResult.Error -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(InstitutionAccountsPreviewEvent.ShowError(parseError(it.throwable)))
                }
            }
        }
    }
}