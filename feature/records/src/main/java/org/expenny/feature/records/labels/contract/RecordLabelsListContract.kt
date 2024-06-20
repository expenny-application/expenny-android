package org.expenny.feature.records.labels.contract

import org.expenny.core.common.extensions.containsIgnoreCase
import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.navargs.StringArrayNavArg

data class RecordLabelsListState(
    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val suggestedLabel: String = "",
    val selectedLabels: List<String> = emptyList(),
    val labels: List<String> = emptyList(),
) {
    val filteredLabels: List<String>
        get() = labels.filter {
            it != searchQuery.trim() &&
                    it.startsWith(searchQuery.trim(), true)
                    && !selectedLabels.containsIgnoreCase(it)
        }
}

sealed interface RecordLabelsListAction {
    class OnLabelAdd(val label: String) : RecordLabelsListAction
    class OnLabelRemove(val label: String) : RecordLabelsListAction
    class OnSearchQueryChange(val query: String) : RecordLabelsListAction
    data object OnConfirmClick : RecordLabelsListAction
    data object OnCloseClick : RecordLabelsListAction
}

sealed interface RecordLabelsListEvent {
    class NavigateBackWithResult(val result: StringArrayNavArg) : RecordLabelsListEvent
    class ShowError(val error: StringResource) : RecordLabelsListEvent
    data object NavigateBack : RecordLabelsListEvent
}