package org.expenny.feature.accounts.contract

import org.expenny.core.common.types.AccountType

data class AccountTypeState(
    val accountTypes: List<AccountType> = AccountType.entries,
    val selectedType: AccountType? = null,
    val isCtaButtonEnabled: Boolean = false,
)

sealed interface AccountTypeAction {
    class OnAccountTypeSelect(val accountType: AccountType) : AccountTypeAction
    data object OnCtaButtonClick : AccountTypeAction
    data object OnBackClick : AccountTypeAction
}

sealed interface AccountTypeEvent {
    data object NavigateToInstitutionCountriesList : AccountTypeEvent
    data object NavigateToCreateAccount : AccountTypeEvent
    data object NavigateBack : AccountTypeEvent
}