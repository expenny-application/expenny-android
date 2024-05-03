package org.expenny.feature.institution.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.InstitutionUi

data class InstitutionsListState(
    val searchQuery: String = "",
    val institutions: List<InstitutionUi> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface InstitutionsListAction {
    class OnInstitutionSelect(val institutionId: String) : InstitutionsListAction
    class OnSearchQueryChange(val query: String) : InstitutionsListAction
    data object OnBackClick : InstitutionsListAction
}

sealed interface InstitutionsListEvent {
    class ShowError(val message: StringResource) : InstitutionsListEvent
    class NavigateToInstitutionRequisition(val institutionId: String) : InstitutionsListEvent
    data object NavigateBack : InstitutionsListEvent
}