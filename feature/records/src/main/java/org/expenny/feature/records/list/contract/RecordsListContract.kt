package org.expenny.feature.records.list.contract

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
import org.expenny.feature.records.list.reducer.FilterSelectionsStateReducer

data class RecordsListState(
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

sealed interface RecordsListAction {
    sealed interface Dialog : RecordsListAction {
        class OnIntervalTypeSelect(val selection: SingleSelectionUi<IntervalType>) : Dialog
        class OnRecordActionSelect(val action: RecordActionType) : Dialog
        class OnAccountsSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnLabelsSelect(val selection: MultiSelectionUi<Int>) : Dialog
        class OnCategoriesSelect(val selection: MultiSelectionUi<Long>) : Dialog
        class OnRecordTypesSelect(val selection: MultiSelectionUi<RecordType>) : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnRecordLongClick(val id: Long) : RecordsListAction
    class OnRecordClick(val id: Long) : RecordsListAction
    class OnFilterClick(val filterType: RecordsFilterType) : RecordsListAction
    data object OnDeleteSelectedRecordsClick : RecordsListAction
    data object OnClearFilterClick : RecordsListAction
    data object OnSelectIntervalTypeClick : RecordsListAction
    data object OnNextIntervalClick : RecordsListAction
    data object OnPreviousIntervalClick : RecordsListAction
    data object OnAddRecordClick : RecordsListAction
    data object OnExitSelectionModeClick : RecordsListAction
    data object OnSelectAllClick : RecordsListAction
    data object OnBackClick : RecordsListAction
}

sealed interface RecordsListEvent {
    class ShowMessage(val message: StringResource) : RecordsListEvent
    class NavigateToEditRecord(val id: Long) : RecordsListEvent
    class NavigateToCloneRecord(val id: Long) : RecordsListEvent
    data object NavigateToCreateRecord : RecordsListEvent
    data object NavigateBack : RecordsListEvent
}