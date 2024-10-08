package org.expenny.feature.institution.requisition.contract

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
    data object NavigateToBackToAccountsList : InstitutionRequisitionEvent
    data object NavigateBack : InstitutionRequisitionEvent
}