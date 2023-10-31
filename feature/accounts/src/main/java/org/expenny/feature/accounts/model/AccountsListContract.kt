package org.expenny.feature.accounts.model

import kotlinx.collections.immutable.persistentListOf
import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.data.selection.Selection
import org.expenny.core.ui.data.ui.AccountUi

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.accounts_label),
    val selection: Selection<Long>? = null,
    val showConfirmButton: Boolean = false,
    val accounts: List<AccountUi> = persistentListOf(),
)

sealed interface Action {
    class OnAccountClick(val id: Long) : Action
    object OnAccountAddClick : Action
    object OnConfirmSelectionClick : Action
    object OnBackClick : Action
}

sealed interface Event {
    class NavigateBackWithResult(val result: NavArgResult) : Event
    class NavigateToEditAccount(val id: Long) : Event
    object NavigateToCreateAccount : Event
    object NavigateBack : Event
}