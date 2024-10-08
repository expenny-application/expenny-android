package org.expenny.feature.records.list.reducer

import kotlinx.coroutines.CoroutineScope
import org.expenny.core.common.extensions.toInt
import org.expenny.core.common.types.RecordType
import org.expenny.core.ui.reducers.ContainerStateReducer
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class FilterSelectionsStateReducer(
    scope: CoroutineScope,
    initialState: State = State()
) : ContainerStateReducer<FilterSelectionsStateReducer.State>(initialState, scope) {

    fun onAccountsSelectionUpdate(selection: List<Long>) = intent {
        reduce { state.copy(accountsSelection = selection) }
    }

    fun onCategoriesSelectionUpdate(selection: List<Long>) = intent {
        reduce { state.copy(categoriesSelection = selection) }
    }

    fun onLabelsSelectionUpdate(selection: List<Int>) = intent {
        reduce { state.copy(labelsSelection = selection) }
    }

    fun onRecordTypesSelectionUpdate(selection: List<RecordType>) = intent {
        reduce { state.copy(recordTypesSelection = selection) }
    }

    fun onWithoutCategorySelect() = intent {
        reduce { state.copy(withoutCategory = !state.withoutCategory) }
    }

    fun onClearFilter() = intent {
        reduce {
            state.copy(
                recordTypesSelection = emptyList(),
                accountsSelection = emptyList(),
                categoriesSelection = emptyList(),
                labelsSelection = emptyList(),
                withoutCategory = false
            )
        }
    }

    data class State(
        val recordTypesSelection: List<RecordType> = emptyList(),
        val accountsSelection: List<Long> = emptyList(),
        val categoriesSelection: List<Long> = emptyList(),
        val labelsSelection: List<Int> = emptyList(),
        val withoutCategory: Boolean = false,
    ) : ContainerStateReducer.State {

        val selectionSize
            get() = listOf(
                recordTypesSelection.size,
                accountsSelection.size,
                categoriesSelection.size,
                labelsSelection.size,
                withoutCategory.toInt()
            ).sum()
    }
}