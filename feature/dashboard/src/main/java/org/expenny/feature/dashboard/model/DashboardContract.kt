package org.expenny.feature.dashboard.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.types.PeriodType
import org.expenny.core.common.types.DashboardWidgetType
import org.expenny.core.common.types.RecordType
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg
import org.expenny.core.ui.data.AccountNameUi

data class State(
    val showAddRecordDialog: Boolean = false,
    val displayCurrency: String? = null,
    val selectAllAccounts: Boolean = true,
    val selectedAccounts: List<AccountNameUi> = emptyList(),
    val accounts: List<AccountNameUi> = emptyList(),
    val periodTypes: ImmutableList<PeriodType> = PeriodType.values().toList().toImmutableList(),
    val currentPeriodType: PeriodType = periodTypes.first(),
    val widgets: ImmutableList<DashboardWidgetType> = DashboardWidgetType.values().toList().toImmutableList(),
    val balanceData: DashboardBalanceUi = DashboardBalanceUi(),
    val expensesData: DashboardExpensesUi = DashboardExpensesUi(),
)

sealed interface Action {
    class OnExpensesPeriodTypeChange(val periodType: PeriodType) : Action
    class OnCategoryExpensesSelect(val index: Int) : Action
    class OnWidgetClick(val widget: DashboardWidgetType) : Action
    class OnAccountSelect(val account: AccountNameUi) : Action
    class OnDisplayCurrencySelect(val id: Long) : Action
    class OnAddRecord(val recordType: RecordType) : Action
    object OnAddRecordClick : Action
    object OnAddRecordDialogDismiss : Action
    object OnShowMoreRecordsClick : Action
    object OnAllAccountsSelect : Action
    object OnCategoryExpensesDeselect : Action
    object OnDisplayCurrencyClick : Action
    object OnCreateAccountClick : Action
}

sealed interface Event {
    class NavigateToDisplayCurrencySelection(val selectedId: Long?) : Event
    class NavigateToRecords(val filter: RecordsListFilterNavArg? = null) : Event
    class NavigateToCreateRecord(val recordType: RecordType) : Event
    object NavigateToCreateAccount : Event
    object NavigateToAccounts : Event
}