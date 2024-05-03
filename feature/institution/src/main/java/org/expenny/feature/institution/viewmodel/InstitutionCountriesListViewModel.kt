package org.expenny.feature.institution.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.domain.usecase.institution.GetInstitutionCountriesUseCase
import org.expenny.core.model.resource.RemoteResult
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.InstitutionCountryUi
import org.expenny.core.ui.mapper.InstitutionCountryMapper
import org.expenny.feature.institution.contract.InstitutionCountriesListAction
import org.expenny.feature.institution.contract.InstitutionCountriesListEvent
import org.expenny.feature.institution.contract.InstitutionCountriesListState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InstitutionCountriesListViewModel @Inject constructor(
    private val getInstitutionCountries: GetInstitutionCountriesUseCase,
    private val institutionCountryMapper: InstitutionCountryMapper
) : ExpennyViewModel<InstitutionCountriesListAction>(),
    ContainerHost<InstitutionCountriesListState, InstitutionCountriesListEvent> {

    private var latestCountries: List<InstitutionCountryUi> = emptyList()

    override val container = container<InstitutionCountriesListState, InstitutionCountriesListEvent>(
        initialState = InstitutionCountriesListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToInstitutionCurrencies() }
        }
    }

    override fun onAction(action: InstitutionCountriesListAction) {
        when (action) {
            is InstitutionCountriesListAction.OnCountrySelect -> handleOnCountrySelect(action)
            is InstitutionCountriesListAction.OnBackClick -> handleOnBackClick()
            is InstitutionCountriesListAction.OnSearchQueryChange -> handleOnSearchQueryChange(action)
        }
    }

    private fun handleOnCountrySelect(action: InstitutionCountriesListAction.OnCountrySelect) = intent {
        postSideEffect(InstitutionCountriesListEvent.NavigateToInstitutionsList(action.countryCode))
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(InstitutionCountriesListEvent.NavigateBack)
    }

    private fun handleOnSearchQueryChange(action: InstitutionCountriesListAction.OnSearchQueryChange) {
        blockingIntent {
            reduce { state.copy(searchQuery = action.query) }
        }
        intent {
            reduce {
                state.copy(
                    countries = latestCountries.filter {
                        it.country.startsWith(action.query, true)
                    }
                )
            }
        }
    }

    private fun subscribeToInstitutionCurrencies() = intent {
        getInstitutionCountries().collect {
            when (it) {
                is RemoteResult.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is RemoteResult.Success -> {
                    latestCountries = institutionCountryMapper(it.data)
                    reduce {
                        state.copy(
                            isLoading = false,
                            countries = latestCountries
                        )
                    }
                }
                is RemoteResult.Error -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(InstitutionCountriesListEvent.ShowError(parseError(it.throwable)))
                }
            }
        }
    }
}