package org.expenny.feature.records

import org.expenny.core.common.types.DateRangeSpan
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.utils.StringResource
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.feature.records.model.RecordActionType
import org.expenny.feature.records.model.RecordsFilterType
import org.expenny.feature.records.model.SelectionFilterDataUi
import org.expenny.core.ui.reducers.DateRangeSpanStateReducer
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer

data class State(
    val dialog: Dialog? = null,
    val isSelectionMode: Boolean = false,
    val records: List<RecordUi> = listOf(),
    val recordsSelection: MultiSelection<Long> = MultiSelection(emptyList()),
    val filterTypes: List<RecordsFilterType> = emptyList(),
    val dateRangeSpans: List<DateRangeSpan> = DateRangeSpan.spans,
    val dateRangeSpanState: DateRangeSpanStateReducer.State = DateRangeSpanStateReducer.State(),
    val filterSelectionsState: FilterSelectionsStateReducer.State = FilterSelectionsStateReducer.State(),
    val selectionFilterData: SelectionFilterDataUi = SelectionFilterDataUi(),
) {
    sealed interface Dialog {
        data object DeleteRecordDialog : Dialog
        data object RecordActionsDialog : Dialog
        data object DateRangeSpanDialog : Dialog
        data object RecordTypesDialog : Dialog
        data object AccountsDialog : Dialog
        data object CategoriesDialog : Dialog
        data object LabelsDialog : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnDateRangeSpanSelect(val dateRangeSpan: DateRangeSpan) : Dialog
        class OnRecordActionSelect(val action: RecordActionType) : Dialog
        class OnAccountsSelect(val selection: List<Long>) : Dialog
        class OnLabelsSelect(val selection: List<Long>) : Dialog
        class OnCategoriesSelect(val selection: List<Long>) : Dialog
        class OnRecordTypesSelect(val selection: List<RecordType>) : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnRecordLongClick(val id: Long) : Action
    class OnRecordClick(val id: Long) : Action
    class OnFilterClick(val filterType: RecordsFilterType) : Action
    data object OnDeleteSelectedRecordsClick : Action
    data object OnClearFilterClick : Action
    data object OnSelectDateRangeSpanClick : Action
    data object OnNextDateRangeClick : Action
    data object OnPreviousDateRangeClick : Action
    data object OnAddRecordClick : Action
    data object OnExitSelectionModeClick : Action
    data object OnSelectAllClick : Action
    data object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToEditRecord(val id: Long) : Event
    class NavigateToCloneRecord(val id: Long) : Event
    data object NavigateToCreateRecord : Event
    data object NavigateBack : Event
}