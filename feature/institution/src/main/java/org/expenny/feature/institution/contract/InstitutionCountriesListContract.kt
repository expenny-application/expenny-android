package org.expenny.feature.institution.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.InstitutionCountryUi

data class InstitutionCountriesListState(
    val searchQuery: String = "",
    val countries: List<InstitutionCountryUi> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface InstitutionCountriesListAction {
    class OnCountrySelect(val countryCode: String) : InstitutionCountriesListAction
    class OnSearchQueryChange(val query: String) : InstitutionCountriesListAction
    data object OnBackClick : InstitutionCountriesListAction
}

sealed interface InstitutionCountriesListEvent {
    class ShowError(val message: StringResource) : InstitutionCountriesListEvent
    class NavigateToInstitutionsList(val countryCode: String) : InstitutionCountriesListEvent
    data object NavigateBack : InstitutionCountriesListEvent
}