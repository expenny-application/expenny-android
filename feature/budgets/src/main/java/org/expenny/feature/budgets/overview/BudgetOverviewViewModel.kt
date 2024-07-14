package org.expenny.feature.budgets.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.budget.CreateBudgetUseCase
import org.expenny.core.domain.usecase.budget.GetBudgetsUseCase
import org.expenny.core.domain.usecase.budget.GetChronologicalBudgetsUseCase
import org.expenny.core.domain.usecase.budgetgroup.GetPeriodicBudgetGroupUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.mapper.AccountNameMapper
import org.expenny.core.ui.mapper.BudgetGroupMapper
import org.expenny.core.ui.mapper.CategoryMapper
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import org.expenny.feature.budgets.navArgs
import org.expenny.feature.budgets.overview.contract.BudgetOverviewAction
import org.expenny.feature.budgets.overview.contract.BudgetOverviewEvent
import org.expenny.feature.budgets.overview.contract.BudgetOverviewState
import org.expenny.feature.budgets.overview.navigation.BudgetOverviewNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BudgetOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPeriodicBudget: GetPeriodicBudgetGroupUseCase,
    private val getChronologicalBudgets: GetChronologicalBudgetsUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val getAccounts: GetAccountsUseCase,
    private val accountNameMapper: AccountNameMapper,
    private val budgetGroupMapper: BudgetGroupMapper,
    private val createBudget: CreateBudgetUseCase,
    private val categoryMapper: CategoryMapper
) : ExpennyViewModel<BudgetOverviewAction>(), ContainerHost<BudgetOverviewState, BudgetOverviewEvent> {

    private val budgetType: BudgetType = savedStateHandle.navArgs<BudgetOverviewNavArgs>().budgetType
    private val budgetGroupId: Long = savedStateHandle.navArgs<BudgetOverviewNavArgs>().budgetGroupId
    private val periodicBudgetIntervalType: IntervalType? = savedStateHandle.navArgs<BudgetOverviewNavArgs>().intervalType

    private val intervalTypeReducer = IntervalTypeStateReducer(viewModelScope)
    private val selectedAccountIds = MutableStateFlow<List<Long>>(emptyList())
    private val selectedCurrency = MutableStateFlow<Currency?>(null)

    override val container = container<BudgetOverviewState, BudgetOverviewEvent>(
        initialState = BudgetOverviewState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            if (periodicBudgetIntervalType != null) {
                intervalTypeReducer.onIntervalTypeChange(periodicBudgetIntervalType)
            }
            launch { subscribeToBudget() }
            launch { subscribeToDefaultDisplayCurrency() }
            launch { subscribeToAccounts() }
            launch { subscribeToIntervalReducer() }
            launch { subscribeToDateRangeBounds() }
        }
    }


    override fun onAction(action: BudgetOverviewAction) {
        when (action) {
            is BudgetOverviewAction.OnDisplayCurrencySelect -> handleOnDisplayCurrencySelect(action)
            is BudgetOverviewAction.OnDisplayCurrencyClick -> handleOnDisplayCurrencyClick()
            is BudgetOverviewAction.OnAllAccountsSelect -> handleOnAllAccountsSelect()
            is BudgetOverviewAction.OnAccountSelect -> {}
            is BudgetOverviewAction.OnNextIntervalClick -> handleOnNextIntervalClick()
            is BudgetOverviewAction.OnPreviousIntervalClick -> handleOnPreviousIntervalClick()
            is BudgetOverviewAction.OnBackClick -> handleOnBackClick()
            is BudgetOverviewAction.OnAddBudgetLimitClick -> handleOnAddBudgetLimitClick()
            is BudgetOverviewAction.OnBudgetLimitClick -> handleOnBudgetLimitClick(action)
        }
    }

    private fun handleOnBudgetLimitClick(action: BudgetOverviewAction.OnBudgetLimitClick) = intent {
        val excludeCategoriesIds = state.overview.budgets.map { it.category.id }.toLongArray()
        val event = BudgetOverviewEvent.NavigateToBudgetLimitDetails(
            budgetId = action.budgetId,
            budgetGroupId = budgetGroupId,
            budgetType = budgetType,
            startDate = intervalTypeReducer.state.dateRange.start,
            endDate = intervalTypeReducer.state.dateRange.endInclusive,
            excludeCategoriesIds = excludeCategoriesIds
        )
        postSideEffect(event)
    }

    private fun handleOnAddBudgetLimitClick() = intent {
        val excludeCategoriesIds = state.overview.budgets.map { it.category.id }.toLongArray()
        val event = BudgetOverviewEvent.NavigateToBudgetLimitDetails(
            budgetId = null,
            budgetGroupId = budgetGroupId,
            budgetType = budgetType,
            startDate = intervalTypeReducer.state.dateRange.start,
            endDate = intervalTypeReducer.state.dateRange.endInclusive,
            excludeCategoriesIds = excludeCategoriesIds
        )
        postSideEffect(event)
    }

    private fun handleOnNextIntervalClick() {
        if (intervalTypeReducer.state.offset < 1) {
            intervalTypeReducer.onNextInterval()
        }
    }

    private fun handleOnPreviousIntervalClick() {
        intervalTypeReducer.onPreviousInterval()
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(BudgetOverviewEvent.NavigateBack)
    }

    private fun handleOnDisplayCurrencyClick() = intent {
        postSideEffect(BudgetOverviewEvent.NavigateToDisplayCurrencySelection(selectedCurrency.value?.id))
    }

    private fun handleOnDisplayCurrencySelect(action: BudgetOverviewAction.OnDisplayCurrencySelect) = intent {
        handleOnDisplayCurrencySelect(getCurrency(GetCurrencyUseCase.Params(action.id)).first()!!)
    }

    private fun handleOnDisplayCurrencySelect(currency: Currency) = intent {
        selectedCurrency.value = currency
        reduce {
            state.copy(displayCurrency = currency.unit.code)
        }
    }

    private fun handleOnAllAccountsSelect() = intent {
        selectedAccountIds.value = state.accounts.map { it.id }
        reduce {
            state.copy(
                selectAllAccounts = true,
                selectedAccounts = state.accounts.toImmutableList(),
            )
        }
    }

    private fun subscribeToDefaultDisplayCurrency() = intent {
        getMainCurrency().filterNotNull().collect {
            handleOnDisplayCurrencySelect(it)
        }
    }

    private fun subscribeToAccounts() = intent {
        getAccounts().collect {
            reduce { state.copy(accounts = accountNameMapper(it)) }
            handleOnAllAccountsSelect()
        }
    }

    private fun subscribeToBudget() = intent {
        intervalTypeReducer.stateFlow
            .flatMapLatest { getPeriodicBudget(budgetGroupId, it.dateRange) }
            .filterNotNull()
            .collect {
                reduce {
                    state.copy(overview = budgetGroupMapper(it))
                }
        }
    }

    private fun subscribeToIntervalReducer() = intent {
        intervalTypeReducer.container.stateFlow.collect {
            reduce { state.copy(intervalState = it) }
        }
    }

    private fun subscribeToDateRangeBounds() = intent {
        getChronologicalBudgets(budgetGroupId).collect {
            val dateRangeLowerBound = it.firstOrNull()?.startDate ?: intervalTypeReducer.state.dateRange.start
            val dateRangeUpperBound = intervalTypeReducer.getNextInterval().endInclusive

            reduce {
                state.copy(dateRangeFilterBounds = dateRangeLowerBound.rangeTo(dateRangeUpperBound))
            }
        }
    }
}