package org.expenny.feature.institution.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.domain.usecase.institution.CreateInstitutionRequisitionUseCase
import org.expenny.core.domain.usecase.institution.GetInstitutionsUseCase
import org.expenny.core.model.resource.RemoteResult
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.institution.contract.InstitutionsListAction
import org.expenny.feature.institution.contract.InstitutionsListEvent
import org.expenny.feature.institution.contract.InstitutionsListEvent.NavigateToInstitutionRequisition
import org.expenny.feature.institution.contract.InstitutionsListEvent.NavigateBack
import org.expenny.feature.institution.contract.InstitutionsListEvent.ShowError
import org.expenny.feature.institution.contract.InstitutionsListState
import org.expenny.feature.institution.navigation.InstitutionsListNavArgs
import org.expenny.core.ui.data.InstitutionUi
import org.expenny.core.ui.mapper.InstitutionMapper
import org.expenny.feature.institution.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InstitutionsListViewModel @Inject constructor(
    private val getInstitutions: GetInstitutionsUseCase,
    private val createInstitutionRequisition: CreateInstitutionRequisitionUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val institutionMapper: InstitutionMapper
) : ExpennyViewModel<InstitutionsListAction>(),
    ContainerHost<InstitutionsListState, InstitutionsListEvent> {

    private val countryCode = savedStateHandle.navArgs<InstitutionsListNavArgs>().countryCode
    private var latestInstitutions: List<InstitutionUi> = emptyList()

    override val container = container<InstitutionsListState, InstitutionsListEvent>(
        initialState = InstitutionsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToInstitutions() }
        }
    }

    override fun onAction(action: InstitutionsListAction) {
        when (action) {
            is InstitutionsListAction.OnInstitutionSelect -> handleOnInstitutionSelect(action)
            is InstitutionsListAction.OnSearchQueryChange -> handleOnSearchQueryChange(action)
            is InstitutionsListAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnInstitutionSelect(action: InstitutionsListAction.OnInstitutionSelect) = intent {
        postSideEffect(NavigateToInstitutionRequisition(action.institutionId))
    }

    private fun handleOnSearchQueryChange(action: InstitutionsListAction.OnSearchQueryChange) {
        blockingIntent {
            reduce { state.copy(searchQuery = action.query) }
        }
        intent {
            reduce {
                state.copy(
                    institutions = latestInstitutions.filter {
                        it.name.contains(action.query)
                    }
                )
            }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(NavigateBack)
    }

    private fun subscribeToInstitutions() = intent {
        getInstitutions(GetInstitutionsUseCase.Params(countryCode)).collect {
            when (it) {
                is RemoteResult.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is RemoteResult.Success -> {
                    latestInstitutions = institutionMapper(it.data)
                    reduce {
                        state.copy(
                            isLoading = false,
                            institutions = latestInstitutions,
                        )
                    }
                }
                is RemoteResult.Error -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ShowError(parseError(it.throwable)))
                }
            }
        }
    }
}