package org.expenny.feature.institution.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.expenny.core.domain.usecase.institution.GetInstitutionsUseCase
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.usecase.institution.GetInstitutionCountriesUseCase
import org.expenny.core.model.common.Country
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.CountryUi
import org.expenny.feature.institution.contract.InstitutionsListAction
import org.expenny.feature.institution.contract.InstitutionsListEvent
import org.expenny.feature.institution.contract.InstitutionsListEvent.NavigateToInstitutionRequisition
import org.expenny.feature.institution.contract.InstitutionsListEvent.NavigateBack
import org.expenny.feature.institution.contract.InstitutionsListEvent.ShowError
import org.expenny.feature.institution.contract.InstitutionsListState
import org.expenny.core.ui.data.InstitutionUi
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.mapper.CountryMapper
import org.expenny.core.ui.mapper.InstitutionMapper
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
    private val getInstitutionCountries: GetInstitutionCountriesUseCase,
    private val institutionMapper: InstitutionMapper,
    private val countryMapper: CountryMapper
) : ExpennyViewModel<InstitutionsListAction>(),
    ContainerHost<InstitutionsListState, InstitutionsListEvent> {

    private var latestInstitutions: List<InstitutionUi> = emptyList()
    private var countries: List<Country> = emptyList()

    private val countryCodeState = MutableStateFlow<String?>(null)
    private val institutionsListLoadingState = MutableStateFlow(true)
    private val countriesListLoadingState = MutableStateFlow(true)

    override val container = container<InstitutionsListState, InstitutionsListEvent>(
        initialState = InstitutionsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToLoadingStates() }
            launch { subscribeToCountries() }
            launch { subscribeToInstitutions() }
        }
    }

    override fun onAction(action: InstitutionsListAction) {
        when (action) {
            is InstitutionsListAction.OnInstitutionSelect -> handleOnInstitutionSelect(action)
            is InstitutionsListAction.OnSearchQueryChange -> handleOnSearchQueryChange(action)
            is InstitutionsListAction.OnSelectCountryClick -> handleOnSelectCountryClick()
            is InstitutionsListAction.OnBackClick -> handleOnBackClick()
            is InstitutionsListAction.Dialog.OnCountrySelect -> handleOnCountrySelect(action)
            is InstitutionsListAction.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
        }
    }

    private fun handleOnDialogDismiss() = intent {
        reduce { state.copy(countrySelectionDialog = null) }
    }

    private fun handleOnCountrySelect(action: InstitutionsListAction.Dialog.OnCountrySelect) = intent {
        countryCodeState.value = action.selection.value?.code
        reduce {
            state.copy(
                country = action.selection.value,
                countrySelectionDialog = null
            )
        }
    }

    private fun handleOnSelectCountryClick() = intent {
        if (countries.isNotEmpty()) {
            reduce {
                state.copy(
                    countrySelectionDialog = InstitutionsListState.CountrySelectionDialog(
                        data = countries.mapToItemUi(),
                        selection = SingleSelectionUi(state.country)
                    )
                )
            }
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
                        it.name.contains(action.query, ignoreCase = true)
                    }
                )
            }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(NavigateBack)
    }

    private fun subscribeToLoadingStates() = intent {
        combine(
            institutionsListLoadingState,
            countriesListLoadingState,
        ) { isInstitutionsListLoading, isCountriesListLoading ->
            isInstitutionsListLoading || isCountriesListLoading
        }.collect { isLoading ->
            reduce { state.copy(isLoading = isLoading) }
        }
    }

    private fun subscribeToCountries() = intent {
        getInstitutionCountries().collect {
            when (it) {
                is RemoteResult.Loading -> {
                    countriesListLoadingState.value = true
                }
                is RemoteResult.Success -> {
                    countriesListLoadingState.value = false
                    countries = it.data
                }
                is RemoteResult.Error -> {
                    countriesListLoadingState.value = false
                }
            }
        }
    }

    private fun subscribeToInstitutions() = intent {
        countryCodeState.flatMapLatest {
            getInstitutions(GetInstitutionsUseCase.Params(it))
        }.collect {
            when (it) {
                is RemoteResult.Loading -> {
                    institutionsListLoadingState.value = true
                }
                is RemoteResult.Success -> {
                    institutionsListLoadingState.value = false
                    latestInstitutions = institutionMapper(it.data)
                    reduce {
                        state.copy(
                            institutions = latestInstitutions,
                        )
                    }
                }
                is RemoteResult.Error -> {
                    institutionsListLoadingState.value = false
                    postSideEffect(ShowError(parseError(it.throwable)))
                }
            }
        }
    }

    private fun List<Country>.mapToItemUi(): List<ItemUi<CountryUi?>> =
        countryMapper(this).map { ItemUi(it, it.name) }
}