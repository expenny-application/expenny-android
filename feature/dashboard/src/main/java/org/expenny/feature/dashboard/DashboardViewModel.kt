package org.expenny.feature.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.expenny.core.common.types.DashboardWidget
import org.expenny.core.common.types.TimePeriod
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.GetCategoryExpensesUseCase
import org.expenny.core.domain.usecase.account.GetAccountsBalanceUseCase
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg
import org.expenny.core.ui.mapper.*
import org.expenny.feature.dashboard.model.Action
import org.expenny.feature.dashboard.model.Event
import org.expenny.feature.dashboard.model.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCategoryExpenses: GetCategoryExpensesUseCase,
    private val getAccountsBalance: GetAccountsBalanceUseCase,
    private val getAccounts: GetAccountsUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val expensesMapper: ExpensesMapper,
    private val accountNameMapper: AccountNameMapper,
    private val amountMapper: AmountMapper,
    private val recordMapper: RecordMapper,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToDefaultDisplayCurrency() }
            launch { subscribeToAccounts() }
            launch { subscribeToBalanceAndExpenses() }
        }
    }

    private val selectedCurrency = MutableStateFlow<Currency?>(null)
    private val selectedAccountIds = MutableStateFlow<List<Long>>(emptyList())
    private val selectedTimePeriod = MutableStateFlow<TimePeriod>(State().currentTimePeriod)

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnAccountSelect -> handleOnAccountSelect(action)
            is Action.OnAllAccountsSelect -> handleOnAllAccountsSelect()
            is Action.OnCategoryExpensesSelect -> handleOnCategoryExpensesSelect(action)
            is Action.OnCategoryExpensesDeselect -> handleOnCategoryExpensesDeselect()
            is Action.OnExpensesTimeSpanChange -> handleOnExpensesTimeSpanChange(action)
            is Action.OnWidgetClick -> handleOnWidgetClick(action)
            is Action.OnCreateRecordClick -> {}
            is Action.OnCreateAccountClick -> handleOnCreateAccountClick()
            is Action.OnDisplayCurrencyClick -> handleOnDisplayCurrencyClick()
            is Action.OnDisplayCurrencySelect -> handleOnDisplayCurrencySelect(action)
            is Action.OnShowMoreRecordsClick -> handleOnShowMoreRecordsClick()
            is Action.OnAddRecordClick -> handleOnAddRecordClick()
            is Action.OnAddRecord -> handleOnAddRecord(action)
            is Action.OnAddRecordDialogDismiss -> handleOnAddRecordDialogDismiss()
        }
    }

    private fun handleOnAddRecord(action: Action.OnAddRecord) = intent {
        postSideEffect(Event.NavigateToCreateRecord(action.recordType))
    }

    private fun handleOnAddRecordClick() = intent {
        reduce { state.copy(showAddRecordDialog = true) }
    }

    private fun handleOnAddRecordDialogDismiss() = intent {
        reduce { state.copy(showAddRecordDialog = false) }
    }

    private fun handleOnDisplayCurrencySelect(action: Action.OnDisplayCurrencySelect) = intent {
        handleOnDisplayCurrencySelect(getCurrencyUseCase(GetCurrencyUseCase.Params(action.id)).first()!!)
    }

    private fun handleOnDisplayCurrencySelect(currency: Currency) = intent {
        selectedCurrency.value = currency
        reduce {
            state.copy(displayCurrency = currency.unit.code)
        }
    }

    private fun handleOnDisplayCurrencyClick() = intent {
        postSideEffect(Event.NavigateToDisplayCurrencySelection(selectedCurrency.value?.id))
    }

    private fun handleOnAccountSelect(action: Action.OnAccountSelect) = intent {
        val currentSelection = if (state.selectAllAccounts) emptyList() else state.selectedAccounts

        if (currentSelection.isEmpty()) {
            reduce {
                state.copy(selectAllAccounts = false)
            }
        }

        val newSelection = if (currentSelection.contains(action.account)) {
            currentSelection - action.account
        } else {
            currentSelection + action.account
        }

        if (newSelection.isEmpty()) {
            handleOnAllAccountsSelect()
        } else {
            selectedAccountIds.value = newSelection.map { it.id }
            reduce {
                state.copy(selectedAccounts = newSelection.toImmutableList())
            }
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

    private fun handleOnCreateAccountClick() = intent {
        postSideEffect(Event.NavigateToCreateAccount)
    }

    private fun handleOnWidgetClick(action: Action.OnWidgetClick) = intent {
        when (action.widget) {
            DashboardWidget.Accounts -> postSideEffect(Event.NavigateToAccounts)
            DashboardWidget.Records -> postSideEffect(Event.NavigateToRecords())
            else -> {
                // TODO
            }
        }
    }

    private fun handleOnShowMoreRecordsClick() = intent {
        val filter = RecordsListFilterNavArg(accounts = selectedAccountIds.value)
        postSideEffect(Event.NavigateToRecords(filter))
    }

    private fun handleOnExpensesTimeSpanChange(action: Action.OnExpensesTimeSpanChange) = intent {
        selectedTimePeriod.value = action.timePeriod
        reduce {
            state.copy(currentTimePeriod = action.timePeriod)
        }
    }

    private fun handleOnCategoryExpensesDeselect() = intent {
        reduce {
            state.copy(expensesData = state.expensesData.copy(selectedEntry = null))
        }
    }

    private fun handleOnCategoryExpensesSelect(action: Action.OnCategoryExpensesSelect) = intent {
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
        repeatOnSubscription {
            getAccounts().collect {
                accountNameMapper(it).toImmutableList().let { accounts ->
                    reduce {
                        state.copy(accounts = accounts)
                    }
                    handleOnAllAccountsSelect()
                }
            }
        }
    }

    private fun subscribeToBalanceAndExpenses() = intent {
        repeatOnSubscription {
            combine(
                selectedAccountIds,
                selectedTimePeriod,
                selectedCurrency.filterNotNull(),
            ) { accountIds, timeSpan, currency ->
                Triple(accountIds, timeSpan, currency)
            }.flatMapLatest { (accountIds, timeSpan, currency) ->
                combine(
                    getAccountsBalance(GetAccountsBalanceUseCase.Params(accountIds, currency)),
                    getCategoryExpenses(GetCategoryExpensesUseCase.Params(currency, timeSpan, accountIds))
                ) { accountsBalanceData, categoryExpensesData ->
                    accountsBalanceData to categoryExpensesData
                }
            }.onEach { (accountsBalanceData, categoryExpensesData) ->
                reduce {
                    state.copy(
                        expensesData = state.expensesData.copy(
                            selectedEntry = null,
                            totalAmount = amountMapper(categoryExpensesData.totalAmount),
                            entries = expensesMapper(categoryExpensesData.expenses).toImmutableList(),
                        ),
                        balanceData = state.balanceData.copy(
                            lastRecord = accountsBalanceData.lastRecord?.copyWithoutDetails()?.let { recordMapper(it) },
                            amount = amountMapper(accountsBalanceData.balance)
                        )
                    )
                }
            }.collect()
        }
    }

    private fun Record.copyWithoutDetails(): Record {
        return when (this) {
            is Record.Transfer -> copy(labels = emptyList(), description = "", receipts = emptyList())
            is Record.Transaction -> copy(labels = emptyList(), description = "", receipts = emptyList())
        }
    }
}
