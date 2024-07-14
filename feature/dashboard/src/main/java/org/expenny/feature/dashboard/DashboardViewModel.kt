package org.expenny.feature.dashboard

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.expenny.core.common.types.PeriodType
import org.expenny.core.common.types.DashboardWidgetType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.domain.usecase.GetCurrencyAmountSumUseCase
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.category.GetCategoriesStatementsUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg
import org.expenny.core.ui.mapper.AccountNameMapper
import org.expenny.core.ui.mapper.AmountMapper
import org.expenny.core.ui.mapper.ExpensesMapper
import org.expenny.core.ui.mapper.RecordMapper
import org.expenny.core.ui.reducers.AccountsFilterStateReducer
import org.expenny.feature.dashboard.contract.DashboardAction
import org.expenny.feature.dashboard.contract.DashboardEvent
import org.expenny.feature.dashboard.contract.DashboardState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class
DashboardViewModel @Inject constructor(
    private val getCategoriesStatements: GetCategoriesStatementsUseCase,
    private val getCurrencyAmountSum: GetCurrencyAmountSumUseCase,
    private val getRecords: GetRecordsUseCase,
    private val getAccounts: GetAccountsUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val expensesMapper: ExpensesMapper,
    private val accountNameMapper: AccountNameMapper,
    private val amountMapper: AmountMapper,
    private val recordMapper: RecordMapper,
) : ExpennyViewModel<DashboardAction>(), ContainerHost<DashboardState, DashboardEvent> {

    override val container = container<DashboardState, DashboardEvent>(
        initialState = DashboardState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToDefaultDisplayCurrency() }
            launch { subscribeToAccounts() }
            launch { subscribeToBalance() }
            launch { subscribeToExpenses() }
            launch { subscribeToAccountsFilterReducer() }
        }
    }

    private val accountsFilterReducer = AccountsFilterStateReducer(viewModelScope)

    private val selectedCurrency = MutableStateFlow<Currency?>(null)
    private val selectedPeriodType = MutableStateFlow<PeriodType>(DashboardState().currentPeriodType)

    override fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnAccountSelect -> handleOnAccountSelect(action)
            is DashboardAction.OnAllAccountsSelect -> handleOnAllAccountsSelect()
            is DashboardAction.OnCategoryExpensesSelect -> handleOnCategoryExpensesSelect(action)
            is DashboardAction.OnCategoryExpensesDeselect -> handleOnCategoryExpensesDeselect()
            is DashboardAction.OnExpensesPeriodTypeChange -> handleOnExpensesPeriodTypeChange(action)
            is DashboardAction.OnWidgetClick -> handleOnWidgetClick(action)
            is DashboardAction.OnDisplayCurrencyClick -> handleOnDisplayCurrencyClick()
            is DashboardAction.OnDisplayCurrencySelect -> handleOnDisplayCurrencySelect(action)
            is DashboardAction.OnShowMoreRecordsClick -> handleOnShowMoreRecordsClick()
            is DashboardAction.OnAddRecordClick -> handleOnAddRecordClick()
            is DashboardAction.OnAddRecord -> handleOnAddRecord(action)
            is DashboardAction.OnAddRecordDialogDismiss -> handleOnAddRecordDialogDismiss()
        }
    }

    private fun handleOnAddRecord(action: DashboardAction.OnAddRecord) = intent {
        postSideEffect(DashboardEvent.NavigateToCreateRecord(action.recordType))
    }

    private fun handleOnAddRecordClick() = intent {
        reduce { state.copy(showAddRecordDialog = true) }
    }

    private fun handleOnAddRecordDialogDismiss() = intent {
        reduce { state.copy(showAddRecordDialog = false) }
    }

    private fun handleOnDisplayCurrencySelect(action: DashboardAction.OnDisplayCurrencySelect) = intent {
        handleOnDisplayCurrencySelect(getCurrency(GetCurrencyUseCase.Params(action.id)).first()!!)
    }

    private fun handleOnDisplayCurrencySelect(currency: Currency) = intent {
        selectedCurrency.value = currency
        reduce {
            state.copy(displayCurrency = currency.unit.code)
        }
    }

    private fun handleOnDisplayCurrencyClick() = intent {
        postSideEffect(DashboardEvent.NavigateToDisplayCurrencySelection(selectedCurrency.value?.id))
    }

    private fun handleOnAccountSelect(action: DashboardAction.OnAccountSelect) {
        accountsFilterReducer.onSelect(action.id)
    }

    private fun handleOnAllAccountsSelect() {
        accountsFilterReducer.onAllSelect()
    }

    private fun handleOnWidgetClick(action: DashboardAction.OnWidgetClick) = intent {
        when (action.widget) {
            DashboardWidgetType.Accounts -> postSideEffect(DashboardEvent.NavigateToAccounts)
            DashboardWidgetType.Records -> postSideEffect(DashboardEvent.NavigateToRecords())
            DashboardWidgetType.Budgets -> postSideEffect(DashboardEvent.NavigateToBudgets)
            else -> {
                // TODO
            }
        }
    }

    private fun handleOnShowMoreRecordsClick() = intent {
        val filter = RecordsListFilterNavArg(accounts = accountsFilterReducer.state.selectedAccountIds)
        postSideEffect(DashboardEvent.NavigateToRecords(filter))
    }

    private fun handleOnExpensesPeriodTypeChange(action: DashboardAction.OnExpensesPeriodTypeChange) = intent {
        selectedPeriodType.value = action.periodType
        reduce {
            state.copy(currentPeriodType = action.periodType)
        }
    }

    private fun handleOnCategoryExpensesDeselect() = intent {
        reduce {
            state.copy(expensesData = state.expensesData.copy(selectedEntry = null))
        }
    }

    private fun handleOnCategoryExpensesSelect(action: DashboardAction.OnCategoryExpensesSelect) = intent {
        reduce {
            state.copy(
                expensesData = state.expensesData.copy(
                    selectedEntry = state.expensesData.entries.getOrNull(action.index)
                )
            )
        }
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

    private fun subscribeToBalance() = intent {
        repeatOnSubscription {
            combine(
                accountsFilterReducer.stateFlow,
                selectedCurrency.filterNotNull()
            ) { accountsFilterState, currency ->
                Pair(accountsFilterState.selectedAccountIds, currency)
            }.flatMapLatest { (accountIds, currency)->
                combine(
                    getAccounts(GetAccountsUseCase.Params(accountIds)),
                    getRecords(GetRecordsUseCase.Params(accountIds))
                ) { accounts, records ->
                    val balances = accounts.map { it.totalBalance }
                    val totalBalance = getCurrencyAmountSum(balances, currency)
                    val recentRecord = records.firstOrNull()
                    Pair(totalBalance, recentRecord)
                }
            }.collect { (totalBalance, recentRecord) ->
                reduce {
                    state.copy(
                        balanceData = state.balanceData.copy(
                            lastRecord = recentRecord?.copyWithoutDetails()?.let { recordMapper(it) },
                            amount = amountMapper(totalBalance)
                        )
                    )
                }
            }
        }
    }

    private fun subscribeToAccountsFilterReducer() = intent {
        accountsFilterReducer.container.stateFlow.collect {
            reduce { state.copy(accountsFilterState = it) }
        }
    }

    private fun subscribeToExpenses() = intent {
        repeatOnSubscription {
            combine(
                accountsFilterReducer.stateFlow,
                selectedPeriodType,
                selectedCurrency.filterNotNull()
            ) { accountsFilterState, periodType, currency ->
                Triple(accountsFilterState.selectedAccountIds, periodType, currency)
            }.flatMapLatest { (accountIds, periodType, currency) ->
                getCategoriesStatements(
                    accountIds = accountIds,
                    dateTimeRange = periodType.dateTimeRange(),
                    transactionType = TransactionType.Outgoing
                ).map { statements ->
                    val convertedStatements = statements.map {
                        val amount = it.amount.convertTo(currency).abs()
                        it.copy(amount)
                    }
                    val amounts = convertedStatements.map { it.amount }
                    val totalAmount = getCurrencyAmountSum(amounts, currency)
                    Pair(totalAmount, convertedStatements)
                }
            }.collect { (totalAmount, statements) ->
                reduce {
                    state.copy(
                        expensesData = state.expensesData.copy(
                            selectedEntry = null,
                            totalAmount = amountMapper(totalAmount),
                            entries = expensesMapper(statements),
                        )
                    )
                }
            }
        }
    }

    private fun Record.copyWithoutDetails(): Record {
        return when (this) {
            is Record.Transfer -> copy(labels = emptyList(), description = "", attachments = emptyList())
            is Record.Transaction -> copy(labels = emptyList(), description = "", attachments = emptyList())
        }
    }
}
