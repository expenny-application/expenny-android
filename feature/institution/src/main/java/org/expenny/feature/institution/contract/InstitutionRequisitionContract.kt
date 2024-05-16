package org.expenny.feature.institution.contract

import org.expenny.core.common.models.StringResource

data class InstitutionRequisitionState(
    val url: String? = null,
    val redirectUrl: String? = null,
    val isLoading: Boolean = true,
)

sealed interface InstitutionRequisitionAction {
    data object OnRequisitionGranted : InstitutionRequisitionAction
    data object OnRequisitionAborted : InstitutionRequisitionAction
}

sealed interface InstitutionRequisitionEvent {
    class ShowMessage(val message: StringResource) : InstitutionRequisitionEvent
    class ShowError(val message: StringResource) : InstitutionRequisitionEvent
    class NavigateToInstitutionAccountsPreview(val requisitionId: String) : InstitutionRequisitionEvent
    data object NavigateToBackToAccountsList : InstitutionRequisitionEvent
    data object NavigateBack : InstitutionRequisitionEvent
}