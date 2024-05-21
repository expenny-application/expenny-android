package org.expenny.feature.accounts.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.expenny.core.common.types.AccountType
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.accounts.contract.AccountTypeAction
import org.expenny.feature.accounts.contract.AccountTypeEvent
import org.expenny.feature.accounts.contract.AccountTypeState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountTypeViewModel @Inject constructor() :
    ExpennyViewModel<AccountTypeAction>(),
    ContainerHost<AccountTypeState, AccountTypeEvent> {

    override val container = container<AccountTypeState, AccountTypeEvent>(
        initialState = AccountTypeState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    )

    override fun onAction(action: AccountTypeAction) {
        when (action) {
            is AccountTypeAction.OnAccountTypeSelect -> handleOnAccountTypeSelect(action)
            is AccountTypeAction.OnCtaButtonClick -> handleOnCtaButtonClick()
            is AccountTypeAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnAccountTypeSelect(action: AccountTypeAction.OnAccountTypeSelect) = intent {
        reduce {
            state.copy(
                selectedType = action.accountType,
                isCtaButtonEnabled = true
            )
        }
    }

    private fun handleOnCtaButtonClick() = intent {
        when (state.selectedType) {
            AccountType.Bank -> postSideEffect(AccountTypeEvent.NavigateToInstitutionsList)
            AccountType.Local -> postSideEffect(AccountTypeEvent.NavigateToCreateAccount)
            else -> {}
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(AccountTypeEvent.NavigateBack)
    }
}