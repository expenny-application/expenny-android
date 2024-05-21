package org.expenny.feature.accounts.list.contract

import kotlinx.collections.immutable.persistentListOf
import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.data.SelectionUi
import org.expenny.core.ui.data.AccountUi

data class AccountsListState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.accounts_label),
    val selection: SelectionUi<Long>? = null,
    val showConfirmButton: Boolean = false,
    val accounts: List<AccountUi> = persistentListOf(),
)

sealed interface AccountsListAction {
    class OnAccountClick(val id: Long) : AccountsListAction
    data object OnAccountCreateClick : AccountsListAction
    data object OnConfirmSelectionClick : AccountsListAction
    data object OnBackClick : AccountsListAction
}

sealed interface AccountsListEvent {
    class NavigateBackWithResult(val result: NavArgResult) : AccountsListEvent
    class NavigateToEditAccount(val id: Long) : AccountsListEvent
    data object NavigateToAccountType : AccountsListEvent
    data object NavigateToCreateAccount : AccountsListEvent
    data object NavigateBack : AccountsListEvent
}