package org.expenny.feature.budgets.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.budget.GetChronologicalBudgetsUseCase
import org.expenny.core.domain.usecase.budgetgroup.GetPeriodicBudgetGroupUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.mapper.BudgetGroupMapper
import org.expenny.core.ui.reducers.AccountsFilterStateReducer
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BudgetOverviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPeriodicBudget: GetPeriodicBudgetGroupUseCase,
    private val getChronologicalBudgets: GetChronologicalBudgetsUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val getAccounts: GetAccountsUseCase,
    private val budgetGroupMapper: BudgetGroupMapper,
) : ExpennyViewModel<BudgetOverviewAction>(), ContainerHost<BudgetOverviewState, BudgetOverviewEvent> {

    private val budgetType: BudgetType = savedStateHandle.navArgs<BudgetOverviewNavArgs>().budgetType
    private val budgetGroupId: Long = savedStateHandle.navArgs<BudgetOverviewNavArgs>().budgetGroupId
    private val periodicBudgetIntervalType: IntervalType? = savedStateHandle.navArgs<BudgetOverviewNavArgs>().intervalType

    private val intervalTypeReducer = IntervalTypeStateReducer(viewModelScope)
    private val accountsFilterReducer = AccountsFilterStateReducer(viewModelScope)

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
            launch { subscribeToIntervalTypeReducer() }
            launch { subscribeToAccountsFilterReducer() }
            launch { subscribeToDateRangeBounds() }
        }
    }


    override fun onAction(action: BudgetOverviewAction) {
        when (action) {
            is BudgetOverviewAction.OnDisplayCurrencySelect -> handleOnDisplayCurrencySelect(action)
            is BudgetOverviewAction.OnDisplayCurrencyClick -> handleOnDisplayCurrencyClick()
            is BudgetOverviewAction.OnAllAccountsSelect -> handleOnAllAccountsSelect()
            is BudgetOverviewAction.OnAccountSelect -> handleOnAccountSelect(action)
            is BudgetOverviewAction.OnNextIntervalClick -> handleOnNextIntervalClick()
            is BudgetOverviewAction.OnPreviousIntervalClick -> handleOnPreviousIntervalClick()
            is BudgetOverviewAction.OnBackClick -> handleOnBackClick()
            is BudgetOverviewAction.OnAddBudgetLimitClick -> handleOnAddBudgetLimitClick()
            is BudgetOverviewAction.OnBudgetLimitClick -> handleOnBudgetLimitClick(action)
        }
    }

    private fun handleOnBudgetLimitClick(action: BudgetOverviewAction.OnBudgetLimitClick) = intent {
        if (!state.isReadonly) {
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
    }

    private fun handleOnAddBudgetLimitClick() = intent {
        if (!state.isReadonly) {
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

    private fun handleOnAllAccountsSelect() {
        accountsFilterReducer.onAllSelect()
    }

    private fun handleOnAccountSelect(action: BudgetOverviewAction.OnAccountSelect) {
        accountsFilterReducer.onSelect(action.id)
    }

    private fun subscribeToDefaultDisplayCurrency() = intent {
        getMainCurrency().filterNotNull().collect {
            handleOnDisplayCurrencySelect(it)
        }
    }

    private fun subscribeToAccounts() = intent {
        getAccounts().collect { accounts ->
            accountsFilterReducer.onAccountsChange(
                accounts.map { ItemUi(key = it.id, label = it.displayName) }
            )
        }
    }

    private fun subscribeToBudget() = intent {
        combine(
            intervalTypeReducer.stateFlow,
            accountsFilterReducer.stateFlow
        ) { intervalTypeState, accountsFilterState ->
            intervalTypeState to accountsFilterState
        }.flatMapLatest { (intervalTypeState, accountsFilterState) ->
            getPeriodicBudget(
                budgetGroupId = budgetGroupId,
                dateRange = intervalTypeState.dateRange,
                accountIds = accountsFilterState.selectedAccountIds
            )
        }.filterNotNull().collect {
            reduce {
                state.copy(overview = budgetGroupMapper(it))
            }
        }
    }

    private fun subscribeToIntervalTypeReducer() = intent {
        intervalTypeReducer.container.stateFlow.collect {
            reduce {
                state.copy(
                    intervalTypeState = it,
                    isReadonly = it.dateRange.endInclusive < LocalDate.now()
                )
            }
        }
    }

    private fun subscribeToAccountsFilterReducer() = intent {
        accountsFilterReducer.container.stateFlow.collect {
            reduce { state.copy(accountsFilterState = it) }
        }
    }

    private fun subscribeToDateRangeBounds() = intent {
        getChronologicalBudgets(budgetGroupId).collect {
            val dateRangeLowerBound = it.firstOrNull()?.startDate ?: intervalTypeReducer.state.dateRange.start
            val dateRangeUpperBound = intervalTypeReducer.getNextInterval().endInclusive
            val bounds = dateRangeLowerBound.rangeTo(dateRangeUpperBound)

            intervalTypeReducer.onBoundsChange(bounds)
        }
    }
}