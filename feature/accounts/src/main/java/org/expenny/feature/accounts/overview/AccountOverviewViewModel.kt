package org.expenny.feature.accounts.overview

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
import org.expenny.core.common.models.StringResource.Companion.fromArrayRes
import org.expenny.core.common.types.IntervalType
import org.expenny.core.domain.usecase.account.GetAccountTrendUseCase
import org.expenny.core.domain.usecase.account.GetAccountUseCase
import org.expenny.core.domain.usecase.category.GetCategoryStatementsUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.extensions.labelResId
import org.expenny.core.ui.mapper.AmountMapper
import org.expenny.core.ui.mapper.CategoryStatementMapper
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import org.expenny.feature.accounts.navArgs
import org.expenny.feature.accounts.overview.contract.AccountOverviewAction
import org.expenny.feature.accounts.overview.contract.AccountOverviewEvent
import org.expenny.feature.accounts.overview.contract.AccountOverviewState
import org.expenny.feature.accounts.overview.model.AccountOverviewChartUi
import org.expenny.feature.accounts.overview.navigation.AccountOverviewNavArgs
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
): ExpennyViewModel<AccountOverviewAction>(), ContainerHost<AccountOverviewState, AccountOverviewEvent> {

    internal val overviewChartModelProducer = CartesianChartModelProducer.build(Dispatchers.Main)

    private val accountId = savedStateHandle.navArgs<AccountOverviewNavArgs>().accountId!!
    private val intervals = listOf(IntervalType.Week, IntervalType.Month, IntervalType.Year)
    private val intervalReducer = IntervalTypeStateReducer(viewModelScope)
    private val selectedTrendType = MutableStateFlow(AccountOverviewState().trendType)

    override val container = container<AccountOverviewState, AccountOverviewEvent>(
        initialState = AccountOverviewState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToIntervalReducer() }
            launch { subscribeToAccountTrend() }
            launch { subscribeToAccount() }
            launch { subscribeToStatements() }
        }
    }

    private fun subscribeToAccountTrend() = intent {
        combine(
            intervalReducer.stateFlow,
            selectedTrendType
        ) { intervalState, trendType ->
            GetAccountTrendUseCase.Params(
                accountId = accountId,
                dateRange = intervalState.dateRange,
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
        intervalReducer.stateFlow.flatMapLatest {
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

    private fun subscribeToIntervalReducer() = intent {
        intervalReducer.container.stateFlow.collect {
            reduce { state.copy(intervalState = it) }
        }
    }

    override fun onAction(action: AccountOverviewAction) {
        when (action) {
            is AccountOverviewAction.OnTrendTypeChange -> {
                intent {
                    selectedTrendType.value = action.type
                    reduce { state.copy(trendType = action.type) }
                }
            }
            is AccountOverviewAction.OnNextIntervalClick -> {
                intervalReducer.onNextInterval()
            }
            is AccountOverviewAction.OnPreviousIntervalClick -> {
                intervalReducer.onPreviousInterval()
            }
            is AccountOverviewAction.OnSelectIntervalClick -> {
                intent {
                    reduce {
                        state.copy(
                            dialog = AccountOverviewState.Dialog.IntervalTypesDialog(
                                data = intervals.mapToItemUi(),
                                selection = SingleSelectionUi(state.intervalState.intervalType)
                            )
                        )
                    }
                }
            }
            is AccountOverviewAction.Dialog.OnDialogDismiss -> {
                intent {
                    reduce { state.copy(dialog = null) }
                }
            }
            is AccountOverviewAction.Dialog.OnIntervalTypeSelect -> {
                intent {
                    reduce { state.copy(dialog = null) }
                }
                action.selection.value?.let {
                    intervalReducer.onIntervalTypeChange(it)
                }
            }
        }
    }

    @JvmName("mapIntervalTypeToItemUi")
    private fun List<IntervalType>.mapToItemUi() = map {
        ItemUi(it, provideString(fromArrayRes(it.labelResId, it.ordinal)))
    }
}