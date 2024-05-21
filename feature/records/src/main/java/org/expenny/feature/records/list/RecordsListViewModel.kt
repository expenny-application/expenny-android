package org.expenny.feature.records.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.addOrRemoveIfExist
import org.expenny.core.common.models.StringResource.Companion.fromArrayRes
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.RecordActionType
import org.expenny.core.common.types.RecordsFilterType
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.domain.usecase.GetCurrencyAmountSumUseCase
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.category.GetCategoriesUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.record.DeleteRecordUseCase
import org.expenny.core.domain.usecase.record.GetRecordLabelsUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.model.account.Account
import org.expenny.core.model.category.Category
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.MultiSelectionUi
import org.expenny.core.ui.data.RecordUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.extensions.labelResId
import org.expenny.core.ui.mapper.AmountMapper
import org.expenny.core.ui.mapper.RecordMapper
import org.expenny.feature.records.list.navigation.RecordsListNavArgs
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import org.expenny.feature.records.list.contract.RecordsListAction
import org.expenny.feature.records.list.contract.RecordsListEvent
import org.expenny.feature.records.list.contract.RecordsListState
import org.expenny.feature.records.navArgs
import org.expenny.feature.records.list.reducer.FilterSelectionsStateReducer
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
    private val getLabels: GetRecordLabelsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val recordMapper: RecordMapper,
    private val amountMapper: AmountMapper,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrencyAmountSum: GetCurrencyAmountSumUseCase,
) : ExpennyViewModel<RecordsListAction>(), ContainerHost<RecordsListState, RecordsListEvent> {

    private var accounts: List<Account> = emptyList()
    private var categories: List<Category> = emptyList()
    private var labels: List<String> = emptyList()
    private val recordTypes: List<RecordType> = RecordType.values().toList()
    private val intervalTypes: List<IntervalType> = IntervalType.values().toList()

    private var selectedRecordId: Long? = null

    private val intervalTypeReducer = IntervalTypeStateReducer(viewModelScope)
    private val filterSelectionsReducer = FilterSelectionsStateReducer(viewModelScope)

    override val container = container<RecordsListState, RecordsListEvent>(
        initialState = RecordsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToRecords() }
            launch { subscribeToSelectionFilterData() }
            launch { subscribeToFilterSelectionsReducer() }
            launch { subscribeToIntervalReducer() }
            setNavArgs()
        }
    }

    override fun onAction(action: RecordsListAction) {
        when (action) {
            is RecordsListAction.OnBackClick -> handleOnBackClick()
            is RecordsListAction.OnAddRecordClick -> handleOnRecordAddClick()
            is RecordsListAction.OnRecordClick -> handleOnRecordClick(action)
            is RecordsListAction.OnSelectIntervalTypeClick -> handleOnSelectIntervalTypeClick()
            is RecordsListAction.OnRecordLongClick -> handleOnRecordLongClick(action)
            is RecordsListAction.OnExitSelectionModeClick -> handleOnExitSelectionModeClick()
            is RecordsListAction.OnSelectAllClick -> handleOnSelectAllClick()
            is RecordsListAction.OnNextIntervalClick -> handleOnNextIntervalClick()
            is RecordsListAction.OnPreviousIntervalClick -> handleOnPreviousIntervalClick()
            is RecordsListAction.OnClearFilterClick -> handleOnClearFilterClick()
            is RecordsListAction.OnDeleteSelectedRecordsClick -> handleOnDeleteSelectedRecordsClick()
            is RecordsListAction.OnFilterClick -> handleOnFilterClick(action)
            is RecordsListAction.Dialog.OnRecordActionSelect -> handleOnRecordActionSelect(action)
            is RecordsListAction.Dialog.OnDeleteRecordDialogConfirm -> handleOnDeleteRecordDialogConfirm()
            is RecordsListAction.Dialog.OnIntervalTypeSelect -> handleOnIntervalTypeSelect(action)
            is RecordsListAction.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
            is RecordsListAction.Dialog.OnAccountsSelect -> handleOnAccountSelect(action)
            is RecordsListAction.Dialog.OnRecordTypesSelect -> handleOnRecordTypesSelect(action)
            is RecordsListAction.Dialog.OnCategoriesSelect -> handleOnCategoriesSelect(action)
            is RecordsListAction.Dialog.OnLabelsSelect -> handleOnLabelsSelect(action)
        }
    }

    private fun handleOnFilterClick(action: RecordsListAction.OnFilterClick) = intent {
        when (action.filterType) {
            RecordsFilterType.Types -> {
                reduce {
                    state.copy(
                        dialog = RecordsListState.Dialog.RecordTypesDialog(
                            data = recordTypes.mapToItemUi(),
                            selection = MultiSelectionUi(
                                state.filterSelectionsState.recordTypesSelection
                            )
                        )
                    )
                }
            }
            RecordsFilterType.Accounts -> {
                reduce {
                    state.copy(
                        dialog = RecordsListState.Dialog.AccountsDialog(
                            data = accounts.mapToItemUi(),
                            selection = MultiSelectionUi(
                                state.filterSelectionsState.accountsSelection
                            )
                        )
                    )
                }
            }
            RecordsFilterType.Categories -> {
                reduce {
                    state.copy(
                        dialog = RecordsListState.Dialog.CategoriesDialog(
                            data = categories.mapToItemUi(),
                            selection = MultiSelectionUi(
                                state.filterSelectionsState.categoriesSelection
                            )
                        )
                    )
                }
            }
            RecordsFilterType.Labels -> {
                reduce {
                    state.copy(
                        dialog = RecordsListState.Dialog.LabelsDialog(
                            data = labels.mapToItemUi(),
                            selection = MultiSelectionUi(
                                state.filterSelectionsState.labelsSelection
                            )
                        )
                    )
                }
            }
            RecordsFilterType.WithoutCategory -> {
                filterSelectionsReducer.onWithoutCategorySelect()
            }
        }
    }

    private fun handleOnLabelsSelect(action: RecordsListAction.Dialog.OnLabelsSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onLabelsSelectionUpdate(action.selection.value)
    }

    private fun handleOnCategoriesSelect(action: RecordsListAction.Dialog.OnCategoriesSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onCategoriesSelectionUpdate(action.selection.value)
    }

    private fun handleOnRecordTypesSelect(action: RecordsListAction.Dialog.OnRecordTypesSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onRecordTypesSelectionUpdate(action.selection.value)
    }

    private fun handleOnAccountSelect(action: RecordsListAction.Dialog.OnAccountsSelect) {
        handleOnDialogDismiss()
        filterSelectionsReducer.onAccountsSelectionUpdate(action.selection.value)
    }

    private fun handleOnRecordLongClick(action: RecordsListAction.OnRecordLongClick) = intent {
        reduce {
            selectedRecordId = action.id
            state.copy(dialog = RecordsListState.Dialog.RecordActionsDialog)
        }
    }

    private fun handleOnRecordActionSelect(action: RecordsListAction.Dialog.OnRecordActionSelect) {
        handleOnDialogDismiss()

        intent {
            when (action.action) {
                RecordActionType.Delete -> {
                    reduce { state.copy(dialog = RecordsListState.Dialog.DeleteRecordDialog) }
                }
                RecordActionType.Edit -> {
                    postSideEffect(RecordsListEvent.NavigateToEditRecord(selectedRecordId!!))
                    resetSelectedRecordId()
                }
                RecordActionType.Clone -> {
                    postSideEffect(RecordsListEvent.NavigateToCloneRecord(selectedRecordId!!))
                    resetSelectedRecordId()
                }
                RecordActionType.Select -> {
                    reduce { state.copy(isSelectionMode = true) }
                    handleOnRecordClick(RecordsListAction.OnRecordClick(selectedRecordId!!))
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
        val newSelection = if (state.recordsSelection.value.containsAll(recordsIds)) emptyList() else recordsIds
        reduce {
            state.copy(recordsSelection = MultiSelectionUi(newSelection))
        }
    }

    private fun handleOnNextIntervalClick() {
        intervalTypeReducer.onNextInterval()
    }

    private fun handleOnPreviousIntervalClick() {
        intervalTypeReducer.onPreviousInterval()
    }

    private fun handleOnClearFilterClick() {
        filterSelectionsReducer.onClearFilter()
    }

    private fun handleOnDeleteSelectedRecordsClick() = intent {
        if (state.recordsSelection.value.isNotEmpty()) {
            reduce { state.copy(dialog = RecordsListState.Dialog.DeleteRecordDialog) }
        } else {
            postSideEffect(RecordsListEvent.ShowMessage(fromRes(R.string.required_selection_error)))
        }
    }

    private fun handleOnExitSelectionModeClick() = intent {
        reduce {
            state.copy(
                isSelectionMode = false,
                recordsSelection = MultiSelectionUi(emptyList())
            )
        }
    }

    private fun handleOnSelectIntervalTypeClick() = intent {
        reduce {
            state.copy(
                dialog = RecordsListState.Dialog.IntervalTypesDialog(
                    data = intervalTypes.mapToItemUi(),
                    selection = SingleSelectionUi(state.intervalState.intervalType)
                )
            )
        }
    }

    private fun handleOnIntervalTypeSelect(action: RecordsListAction.Dialog.OnIntervalTypeSelect) {
        handleOnDialogDismiss()
        action.selection.value?.let {
            intervalTypeReducer.onIntervalTypeChange(it)
        }
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        handleOnDialogDismiss()

        if (state.isSelectionMode) {
            deleteRecord(*state.recordsSelection.value.toLongArray())
            handleOnExitSelectionModeClick()
        } else {
            deleteRecord(selectedRecordId!!)
            resetSelectedRecordId()
        }
        postSideEffect(RecordsListEvent.ShowMessage(fromRes(R.string.deleted_message)))
    }

    private fun handleOnRecordClick(action: RecordsListAction.OnRecordClick) = intent {
        if (state.isSelectionMode) {
            state.recordsSelection.value.addOrRemoveIfExist(action.id).also {
                reduce {
                    state.copy(
                        recordsSelection = state.recordsSelection.copy(it),
                        isSelectionMode = it.isNotEmpty()
                    )
                }
            }
        } else {
            postSideEffect(RecordsListEvent.NavigateToEditRecord(action.id))
        }
    }

    private fun handleOnRecordAddClick() = intent {
        if (state.isSelectionMode) {
            handleOnExitSelectionModeClick()
        }
        postSideEffect(RecordsListEvent.NavigateToCreateRecord)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(RecordsListEvent.NavigateBack)
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
                    categoriesSelection = filter.categories
                )
            )
        }
    }

    private fun subscribeToRecords() = intent {
        repeatOnSubscription {
            combine(
                intervalTypeReducer.stateFlow,
                filterSelectionsReducer.stateFlow,
            ) { intervalState, filtersState ->
                val selectedLabels = labels.filterIndexed { index, _ ->
                    index in filtersState.labelsSelection
                }.map { it }

                GetRecordsUseCase.Params(
                    labels = selectedLabels,
                    accounts = filtersState.accountsSelection,
                    categories = filtersState.categoriesSelection,
                    dateRange = intervalState.dateRange,
                    withoutCategory = filtersState.withoutCategory,
                    types = filtersState.recordTypesSelection
                )
            }.flatMapLatest { recordsParams ->
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
                Triple(accounts, labels, categories)
            }.collect { (accounts, labels, categories) ->
                this@RecordsListViewModel.accounts = accounts
                this@RecordsListViewModel.labels = labels
                this@RecordsListViewModel.categories = categories

                val recordsFilterTypes = buildList {
                    if (accounts.isNotEmpty()) add(RecordsFilterType.Accounts)
                    if (categories.isNotEmpty()) add(RecordsFilterType.Categories)
                    if (labels.isNotEmpty()) add(RecordsFilterType.Labels)
                    if (recordTypes.isNotEmpty()) add(RecordsFilterType.Types)
                    add(RecordsFilterType.WithoutCategory)
                }
                reduce {
                    state.copy(filterTypes = recordsFilterTypes)
                }
            }
        }
    }

    private fun subscribeToIntervalReducer() = intent {
        repeatOnSubscription {
            intervalTypeReducer.container.stateFlow.collect {
                reduce { state.copy(intervalState = it) }
            }
        }
    }

    @JvmName("mapAccountToItemUi")
    private fun List<Account>.mapToItemUi() = map { ItemUi(it.id, it.displayName) }

    @JvmName("mapCategoryToItemUi")
    private fun List<Category>.mapToItemUi() = map { ItemUi(it.id, it.name) }

    @JvmName("mapLabelToItemUi")
    private fun List<String>.mapToItemUi() = mapIndexed { i, label -> ItemUi(i, label) }

    @JvmName("mapRecordTypeToItemUi")
    private fun List<RecordType>.mapToItemUi() = map {
        ItemUi(it, provideString(fromRes(it.labelResId)))
    }

    @JvmName("mapIntervalTypeToItemUi")
    private fun List<IntervalType>.mapToItemUi() = map {
        ItemUi(it, provideString(fromArrayRes(it.labelResId, it.ordinal)))
    }

    private fun List<Record>.mapToUi(mainCurrency: Currency): List<RecordUi> {
        val grouped = groupBy { it.date.toLocalDate() }

        return buildList {
            grouped.forEach { (date, records) ->
                val dateAmount = records.filterIsInstance<Record.Transaction>()
                    .map { it.typedAmount }
                    .let { getCurrencyAmountSum(it, mainCurrency) }

                add(RecordUi.Header(date.toDateString(), amountMapper(dateAmount)))
                addAll(recordMapper(records))
            }
        }
    }
}