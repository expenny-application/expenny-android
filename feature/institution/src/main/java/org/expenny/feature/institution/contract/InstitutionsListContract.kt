package org.expenny.feature.institution.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.InstitutionCountryUi
import org.expenny.core.ui.data.InstitutionUi
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.SingleSelectionUi

data class InstitutionsListState(
    val searchQuery: String = "",
    val country: InstitutionCountryUi? = null,
    val institutions: List<InstitutionUi> = emptyList(),
    val isLoading: Boolean = true,
    val countrySelectionDialog: CountrySelectionDialog? = null,
) {
    data class CountrySelectionDialog(
        val data: List<ItemUi<InstitutionCountryUi?>>,
        val selection: SingleSelectionUi<InstitutionCountryUi?>
    )
}

sealed interface InstitutionsListAction {
    class OnInstitutionSelect(val institutionId: String) : InstitutionsListAction
    class OnSearchQueryChange(val query: String) : InstitutionsListAction
    data object OnSelectCountryClick : InstitutionsListAction
    data object OnBackClick : InstitutionsListAction

    sealed interface Dialog : InstitutionsListAction {
        class OnCountrySelect(val selection: SingleSelectionUi<InstitutionCountryUi?>) : Dialog
        data object OnDialogDismiss : Dialog
    }
}

sealed interface InstitutionsListEvent {
    class ShowError(val message: StringResource) : InstitutionsListEvent
    class NavigateToInstitutionRequisition(val institutionId: String) : InstitutionsListEvent
    data object NavigateBack : InstitutionsListEvent
}