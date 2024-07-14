package org.expenny.core.ui.reducers

import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.MultiSelectionUi
import org.expenny.core.ui.data.minus
import org.expenny.core.ui.data.plus
import org.expenny.core.ui.extensions.isEmpty
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class AccountsFilterStateReducer(
    scope: CoroutineScope,
    initialState: State = State(),
) : ContainerStateReducer<AccountsFilterStateReducer.State>(initialState, scope) {

    fun onSelect(id: Long) = intent {
        val currentSelection = if (state.selectAll) MultiSelectionUi() else state.selection

        if (currentSelection.isEmpty()) {
            reduce {
                state.copy(selectAll = false)
            }
        }

        val newSelection = if (currentSelection.contains(id)) {
            currentSelection - id
        } else {
            currentSelection + id
        }

        if (newSelection.isEmpty()) {
            onAllSelect()
        } else {
            reduce {
                state.copy(selection = newSelection)
            }
        }
    }

    fun onAllSelect() {
        val selection = MultiSelectionUi(state.accounts.map { it.key })
        intent {
            reduce {
                state.copy(
                    selectAll = true,
                    selection = selection,
                )
            }
        }
    }

    fun onAccountsChange(accounts: List<ItemUi<Long>>) {
        intent {
            reduce {
                state.copy(accounts = accounts)
            }
        }
    }

    data class State(
        val selectAll: Boolean = true,
        val accounts: List<ItemUi<Long>> = emptyList(),
        val selection: MultiSelectionUi<Long> = MultiSelectionUi()
    ) : ContainerStateReducer.State {

        val selectedAccountIds: List<Long>
            get() = selection.value
    }
}