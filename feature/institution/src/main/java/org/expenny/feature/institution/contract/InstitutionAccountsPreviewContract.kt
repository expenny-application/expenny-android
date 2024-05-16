package org.expenny.feature.institution.contract

import org.expenny.core.common.models.StringResource

data class InstitutionAccountsPreviewState(
    val isLoading: Boolean = true,
)

sealed interface InstitutionAccountsPreviewAction {
    data object OnCloseClick : InstitutionAccountsPreviewAction
    data object OnConfirmClick : InstitutionAccountsPreviewAction
}

sealed interface InstitutionAccountsPreviewEvent {
    class ShowError(val message: StringResource) : InstitutionAccountsPreviewEvent
    data object NavigateToBackToAccountsList : InstitutionAccountsPreviewEvent
}