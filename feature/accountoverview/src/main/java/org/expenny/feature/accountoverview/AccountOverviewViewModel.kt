package org.expenny.feature.accountoverview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toClosedDateTimeRange
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.common.types.DateRangeSpan
import org.expenny.core.common.utils.StringResourceProvider
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.usecase.account.GetAccountTrendUseCase
import org.expenny.core.domain.usecase.account.GetAccountUseCase
import org.expenny.core.domain.usecase.category.GetCategoryStatementsUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.ui.data.ui.ItemUi
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.extensions.labelResId
import org.expenny.core.ui.mapper.AmountMapper
import org.expenny.core.ui.mapper.CategoryStatementMapper
import org.expenny.core.ui.reducers.DateRangeSpanStateReducer
import org.expenny.feature.accountoverview.model.AccountOverviewChartUi
import org.expenny.feature.accountoverview.navigation.AccountOverviewNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val amountMapper: AmountMapper,
    private val getAccount: GetAccountUseCase,
    private val getRecords: GetRecordsUseCase,
    private val getAccountTrend: GetAccountTrendUseCase,
    private val getCategoryStatements: GetCategoryStatementsUseCase,
    private val categoryStatementMapper: CategoryStatementMapper,
    private val stringProvider: StringResourceProvider,
): ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    internal val overviewChartModelProducer = CartesianChartModelProducer.build(Dispatchers.Main)

    private val accountId = savedStateHandle.navArgs<AccountOverviewNavArgs>().accountId!!
    private val dateRangeSpans = listOf(DateRangeSpan.Week(), DateRangeSpan.Month(), DateRangeSpan.Year())
    private val dateRangeSpanReducer = DateRangeSpanStateReducer(viewModelScope)
    private val selectedTrendType = MutableStateFlow<AccountTrendType>(State().trendType)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToDateRangeSpanReducer() }
            launch { subscribeToAccountTrend() }
            launch { subscribeToAccount() }
            launch { subscribeToStatements() }
        }
    }

    private fun subscribeToAccountTrend() = intent {
        combine(
            dateRangeSpanReducer.stateFlow,
            selectedTrendType
        ) { dateRangeSpan, trendType ->
            GetAccountTrendUseCase.Params(
                accountId = accountId,
                dateRange = dateRangeSpan.dateRange,
                trendType = trendType
            )
        }.flatMapLatest { params ->
            getAccountTrend(params)
        }.collect {
            reduce {
                state.copy(overviewChart = AccountOverviewChartUi(it))
            }
        }
    }

    private fun subscribeToStatements() = intent {
        dateRangeSpanReducer.stateFlow.flatMapLatest {
            getCategoryStatements(
                GetCategoryStatementsUseCase.Params(
                    accountIds = listOf(accountId),
                    dateTimeRange = it.dateRange.toClosedDateTimeRange(),
                )
            )
        }.collect {
            reduce {
                state.copy(statements = categoryStatementMapper(it))
            }
        }
    }

    private fun subscribeToAccount() = intent {
        getAccount(GetAccountUseCase.Params(accountId))
            .filterNotNull()
            .collect {
                reduce { state.copy(totalValue = amountMapper(it.totalBalance)) }
            }
    }

    private fun subscribeToDateRangeSpanReducer() = intent {
        dateRangeSpanReducer.container.stateFlow.collect {
            reduce { state.copy(dateRangeSpanState = it) }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnTrendTypeChange -> {
                intent {
                    selectedTrendType.value = action.type
                    reduce { state.copy(trendType = action.type) }
                }
            }
            is Action.OnNextDateRangeClick -> {
                dateRangeSpanReducer.onNextDateRange()
            }
            is Action.OnPreviousDateRangeClick -> {
                dateRangeSpanReducer.onPreviousDateRange()
            }
            is Action.OnSelectDateRangeSpanClick -> {
                intent {
                    val selectionIndex = dateRangeSpans.indexOf(state.dateRangeSpanState.dateRangeSpan)
                    reduce {
                        state.copy(
                            dialog = State.Dialog.DateRangeSpanDialog(
                                data = dateRangeSpans.mapToItemUi(),
                                selection = SingleSelectionUi(selectionIndex.toLong())
                            )
                        )
                    }
                }
            }
            is Action.Dialog.OnDialogDismiss -> {
                intent {
                    reduce { state.copy(dialog = null) }
                }
            }
            is Action.Dialog.OnDateRangeSpanSelect -> {
                intent {
                    reduce { state.copy(dialog = null) }
                }
                val dateRangeSpan = action.selection.value?.toInt()?.let { dateRangeSpans.getOrNull(it) }
                if (dateRangeSpan != null) {
                    dateRangeSpanReducer.onDateRangeSpanChange(dateRangeSpan)
                }
            }
        }
    }

    @JvmName("mapDateRangeSpanToItemUi")
    private fun List<DateRangeSpan>.mapToItemUi() = mapIndexed { i, item ->
        ItemUi(i, stringProvider(StringResource.fromArrayRes(item.labelResId, i)))
    }
}