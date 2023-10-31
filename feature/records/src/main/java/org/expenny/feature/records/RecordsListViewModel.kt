package org.expenny.feature.records

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.toggleItem
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.GetCurrencyAmountSumUseCase
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.category.GetCategoriesUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.label.GetLabelsUseCase
import org.expenny.core.domain.usecase.record.DeleteRecordUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.resources.R
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.core.ui.mapper.RecordMapper
import org.expenny.core.ui.mapper.AmountMapper
import org.expenny.feature.records.model.*
import org.expenny.feature.records.reducer.DateRangeStateReducer
import org.expenny.feature.records.reducer.FilterSelectionsStateReducer
import org.expenny.feature.records.navigation.RecordsListNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RecordsListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecords: GetRecordsUseCase,
    private val getAccounts: GetAccountsUseCase,
    private val getLabels: GetLabelsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val recordMapper: RecordMapper,
    private val amountMapper: AmountMapper,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrencyAmountSum: GetCurrencyAmountSumUseCase,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private var selectedRecordId: Long? = null

    private val dateRangeReducer = DateRangeStateReducer(viewModelScope)
    private val filterSelectionsReducer = FilterSelectionsStateReducer(viewModelScope)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToRecords() }
            launch { subscribeToSelectionFilterData() }
            launch { subscribeToFilterSelectionsReducer() }
            launch { subscribeToDateRangeReducer() }
            setNavArgs()
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnAddRecordClick -> handleOnRecordAddClick()
            is Action.OnRecordClick -> handleOnRecordClick(action)
            is Action.OnSelectDateRecurrenceClick -> handleOnSelectDateRecurrenceClick()
            is Action.OnRecordLongClick -> handleOnRecordLongClick(action)
            is Action.OnExitSelectionModeClick -> handleOnExitSelectionModeClick()
            is Action.OnSelectAllClick -> handleOnSelectAllClick()
            is Action.OnNextDateRangeClick -> handleOnNextDateRangeClick()
            is Action.OnPreviousDateRangeClick -> handleOnPreviousDateRangeClick()
            is Action.OnClearFilterClick -> handleOnClearFilterClick()
            is Action.OnDeleteSelectedRecordsClick -> handleOnDeleteSelectedRecordsClick()
            is Action.OnFilterClick -> handleOnFilterClick(action)
            is Action.Dialog.OnRecordActionSelect -> handleOnRecordActionSelect(action)
            is Action.Dialog.OnDeleteRecordDialogConfirm -> handleOnDeleteRecordDialogConfirm()
            is Action.Dialog.OnDateRecurrenceSelect -> handleOnSelectDateRecurrence(action)
            is Action.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
            is Action.Dialog.OnAccountsSelect -> handleOnAccountSelect(action)
            is Action.Dialog.OnRecordTypesSelect -> handleOnRecordTypesSelect(action)
            is Action.Dialog.OnCategoriesSelect -> handleOnCategoriesSelect(action)
            is Action.Dialog.OnLabelsSelect -> handleOnLabelsSelect(action)
        }
    }

    private fun handleOnFilterClick(action: Action.OnFilterClick) = intent {
        when (action.filterType) {
            RecordsFilterType.Types -> {
                reduce { state.copy(dialog = State.Dialog.RecordTypesDialog) }
            }
            RecordsFilterType.Accounts -> {
                reduce { state.copy(dialog = State.Dialog.AccountsDialog) }
            }
            RecordsFilterType.Categories -> {
                reduce { state.copy(dialog = State.Dialog.CategoriesDialog) }
            }
            RecordsFilterType.Labels -> {
                reduce { state.copy(dialog = State.Dialog.LabelsDialog) }
            }
            RecordsFilterType.WithoutCategory -> {
                filterSelectionsReducer.onWithoutCategorySelect()
            }
        }
    }

    private fun handleOnLabelsSelect(action: Action.Dialog.OnLabelsSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onLabelsSelectionUpdate(action.selection)
    }

    private fun handleOnCategoriesSelect(action: Action.Dialog.OnCategoriesSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onCategoriesSelectionUpdate(action.selection)
    }

    private fun handleOnRecordTypesSelect(action: Action.Dialog.OnRecordTypesSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onRecordTypesSelectionUpdate(action.selection)
    }

    private fun handleOnAccountSelect(action: Action.Dialog.OnAccountsSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onAccountsSelectionUpdate(action.selection)
    }

    private fun handleOnRecordLongClick(action: Action.OnRecordLongClick) = intent {
        reduce {
            selectedRecordId = action.id
            state.copy(dialog = State.Dialog.RecordActionsDialog)
        }
    }

    private fun handleOnRecordActionSelect(action: Action.Dialog.OnRecordActionSelect) {
        handleOnDialogDismiss()

        intent {
            when (action.action) {
                RecordActionType.Delete -> {
                    reduce { state.copy(dialog = State.Dialog.DeleteRecordDialog) }
                }
                RecordActionType.Edit -> {
                    postSideEffect(Event.NavigateToEditRecord(selectedRecordId!!))
                    resetSelectedRecordId()
                }
                RecordActionType.Clone -> {
                    postSideEffect(Event.NavigateToCloneRecord(selectedRecordId!!))
                    resetSelectedRecordId()
                }
                RecordActionType.Select -> {
                    reduce { state.copy(isSelectionMode = true) }
                    handleOnRecordClick(Action.OnRecordClick(selectedRecordId!!))
                    resetSelectedRecordId()
                }
            }
        }
    }

    private fun resetSelectedRecordId() {
        selectedRecordId = null
    }

    private fun handleOnSelectAllClick() = intent {
        val recordsIds = state.records.filterIsInstance<RecordUi.Item>().map { it.id }
        val newSelection = if (state.recordsSelection.data.containsAll(recordsIds)) emptyList() else recordsIds
        reduce {
            state.copy(recordsSelection = MultiSelection(newSelection))
        }
    }

    private fun handleOnNextDateRangeClick() {
        dateRangeReducer.onNextDateRange()
    }

    private fun handleOnPreviousDateRangeClick() {
        dateRangeReducer.onPreviousDateRange()
    }

    private fun handleOnClearFilterClick() {
        filterSelectionsReducer.onClearFilter()
    }

    private fun handleOnDeleteSelectedRecordsClick() = intent {
        if (state.recordsSelection.data.isNotEmpty()) {
            reduce { state.copy(dialog = State.Dialog.DeleteRecordDialog) }
        } else {
            postSideEffect(Event.ShowMessage(fromRes(R.string.required_selection_error)))
        }
    }

    private fun handleOnExitSelectionModeClick() = intent {
        reduce {
            state.copy(
                isSelectionMode = false,
                recordsSelection = MultiSelection(emptyList())
            )
        }
    }

    private fun handleOnSelectDateRecurrenceClick() = intent {
        reduce { state.copy(dialog = State.Dialog.DateRecurrenceDialog) }
    }

    private fun handleOnSelectDateRecurrence(action: Action.Dialog.OnDateRecurrenceSelect) {
        handleOnDialogDismiss()
        dateRangeReducer.onDateRecurrenceChange(action.dateRecurrence)
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        handleOnDialogDismiss()

        if (state.isSelectionMode) {
            deleteRecord(*state.recordsSelection.data.toLongArray())
            handleOnExitSelectionModeClick()
        } else {
            deleteRecord(selectedRecordId!!)
            resetSelectedRecordId()
        }
        postSideEffect(Event.ShowMessage(fromRes(R.string.deleted_message)))
    }

    private fun handleOnRecordClick(action: Action.OnRecordClick) = intent {
        if (state.isSelectionMode) {
            state.recordsSelection.data.toggleItem(action.id).also {
                reduce {
                    state.copy(
                        recordsSelection = state.recordsSelection.copy(it),
                        isSelectionMode = it.isNotEmpty()
                    )
                }
            }
        } else {
            postSideEffect(Event.NavigateToEditRecord(action.id))
        }
    }

    private fun handleOnRecordAddClick() = intent {
        if (state.isSelectionMode) {
            handleOnExitSelectionModeClick()
        }
        postSideEffect(Event.NavigateToCreateRecord)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnDialogDismiss() = intent {
        reduce { state.copy(dialog = null) }
    }

    private fun setNavArgs() {
        savedStateHandle.navArgs<RecordsListNavArgs>().filter?.also { filter ->
            filterSelectionsReducer.setState(
                FilterSelectionsStateReducer.State(
                    recordTypesSelection = filter.types,
                    accountsSelection = filter.accounts,
                    categoriesSelection = filter.categories,
                    labelsSelection = filter.labels
                )
            )
        }
    }

    private fun subscribeToRecords() = intent {
        repeatOnSubscription {
            combine(
                dateRangeReducer.stateFlow,
                filterSelectionsReducer.stateFlow,
            ) { dateRangeState, filtersState ->
                GetRecordsUseCase.Params(
                    accounts = filtersState.accountsSelection,
                    labels = filtersState.labelsSelection,
                    categories = filtersState.categoriesSelection,
                    types = filtersState.recordTypesSelection,
                    dateRange = dateRangeState.dateRange,
                    withoutCategory = filtersState.withoutCategory,
                )
            }.distinctUntilChanged().flatMapLatest { recordsParams ->
                combine(
                    getRecords(recordsParams),
                    getMainCurrency()
                ) { records, mainCurrency ->
                    records.mapToUi(mainCurrency!!)
                }
            }.collect {
                reduce { state.copy(records = it) }
            }
        }
    }

    private fun subscribeToFilterSelectionsReducer() = intent {
        repeatOnSubscription {
            filterSelectionsReducer.container.stateFlow.collect {
                reduce { state.copy(filterSelectionsState = it) }
            }
        }
    }

    private fun subscribeToSelectionFilterData() = intent {
        repeatOnSubscription {
            combine(
                getAccounts(),
                getLabels(),
                getCategories(),
            ) { accounts, labels, categories ->
                SelectionFilterDataUi(
                    accounts = accounts.map { Pair(it.id, it.displayName) },
                    categories = categories.map { Pair(it.id, it.name) },
                    labels = labels.map { Pair(it.id, it.name) },
                    recordTypes = RecordType.values().toList()
                )
            }.collect {
                val recordsFilterTypes = buildList {
                    if (it.accounts.isNotEmpty()) add(RecordsFilterType.Accounts)
                    if (it.categories.isNotEmpty()) add(RecordsFilterType.Categories)
                    if (it.labels.isNotEmpty()) add(RecordsFilterType.Labels)
                    add(RecordsFilterType.Types)
                    add(RecordsFilterType.WithoutCategory)
                }
                reduce {
                    state.copy(
                        selectionFilterData = it,
                        filterTypes = recordsFilterTypes
                    )
                }
            }
        }
    }

    private fun subscribeToDateRangeReducer() = intent {
        repeatOnSubscription {
            dateRangeReducer.container.stateFlow.collect {
                reduce { state.copy(dateRangeState = it) }
            }
        }
    }

    private fun List<Record>.mapToUi(mainCurrency: Currency): List<RecordUi> {
        val grouped = groupBy { it.date.toLocalDate() }

        return buildList {
            grouped.forEach { (date, records) ->
                val dateAmount = records.filterIsInstance<Record.Transaction>()
                    .map { it.transactionAmount }
                    .let { getCurrencyAmountSum(it, mainCurrency) }

                add(RecordUi.Header(date.toDateString(), amountMapper(dateAmount)))
                addAll(recordMapper(records))
            }
        }
    }
}