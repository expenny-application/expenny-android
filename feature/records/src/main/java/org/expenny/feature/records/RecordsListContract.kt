package org.expenny.feature.records

import org.expenny.core.common.models.StringResource
import org.expenny.core.ui.data.ui.ItemUi
import org.expenny.core.ui.data.ui.MultiSelectionUi
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.feature.records.model.RecordActionType
import org.expenny.feature.records.model.RecordsFilterType
import org.expenny.core.ui.reducers.DateRangeSpanStateReducer
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer

data class State(
    val dialog: Dialog? = null,
    val isSelectionMode: Boolean = false,
    val records: List<RecordUi> = listOf(),
    val recordsSelection: MultiSelectionUi<Long> = MultiSelectionUi(emptyList()),
    val filterTypes: List<RecordsFilterType> = emptyList(),
    val dateRangeSpanState: DateRangeSpanStateReducer.State = DateRangeSpanStateReducer.State(),
    val filterSelectionsState: FilterSelectionsStateReducer.State = FilterSelectionsStateReducer.State(),
) {
    sealed interface Dialog {
        data object DeleteRecordDialog : Dialog
        data object RecordActionsDialog : Dialog

        data class DateRangeSpanDialog(
            val data: List<ItemUi>,
            val selection: SingleSelectionUi<Long>
        ) : Dialog

        data class RecordTypesDialog(
            val data: List<ItemUi>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog

        data class AccountsDialog(
            val data: List<ItemUi>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog

        data class CategoriesDialog(
            val data: List<ItemUi>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog

        data class LabelsDialog(
            val data: List<ItemUi>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnDateRangeSpanSelect(val selection: SingleSelectionUi<Long>) : Dialog
        class OnRecordActionSelect(val action: RecordActionType) : Dialog
        class OnAccountsSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnLabelsSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnCategoriesSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnRecordTypesSelect(val selection: MultiSelectionUi<Long>) : Dialog
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