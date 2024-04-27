package org.expenny.feature.records

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.RecordActionType
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.RecordsFilterType
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.MultiSelectionUi
import org.expenny.core.ui.data.RecordUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer

data class State(
    val dialog: Dialog? = null,
    val isSelectionMode: Boolean = false,
    val records: List<RecordUi> = listOf(),
    val recordsSelection: MultiSelectionUi<Long> = MultiSelectionUi(emptyList()),
    val filterTypes: List<RecordsFilterType> = emptyList(),
    val intervalState: IntervalTypeStateReducer.State = IntervalTypeStateReducer.State(),
    val filterSelectionsState: FilterSelectionsStateReducer.State = FilterSelectionsStateReducer.State(),
) {
    sealed interface Dialog {
        data object DeleteRecordDialog : Dialog
        data object RecordActionsDialog : Dialog

        data class IntervalTypesDialog(
            val data: List<ItemUi<IntervalType>>,
            val selection: SingleSelectionUi<IntervalType>
        ) : Dialog

        data class RecordTypesDialog(
            val data: List<ItemUi<RecordType>>,
            val selection: MultiSelectionUi<RecordType>
        ) : Dialog

        data class AccountsDialog(
            val data: List<ItemUi<Long>>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog

        data class CategoriesDialog(
            val data: List<ItemUi<Long>>,
            val selection: MultiSelectionUi<Long>
        ) : Dialog

        data class LabelsDialog(
            val data: List<ItemUi<Int>>,
            val selection: MultiSelectionUi<Int>
        ) : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnIntervalTypeSelect(val selection: SingleSelectionUi<IntervalType>) : Dialog
        class OnRecordActionSelect(val action: RecordActionType) : Dialog
        class OnAccountsSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnLabelsSelect(val selection: MultiSelectionUi<Int>) : Dialog
        class OnCategoriesSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnRecordTypesSelect(val selection: MultiSelectionUi<RecordType>) : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnRecordLongClick(val id: Long) : Action
    class OnRecordClick(val id: Long) : Action
    class OnFilterClick(val filterType: RecordsFilterType) : Action
    data object OnDeleteSelectedRecordsClick : Action
    data object OnClearFilterClick : Action
    data object OnSelectIntervalTypeClick : Action
    data object OnNextIntervalClick : Action
    data object OnPreviousIntervalClick : Action
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