package org.expenny.feature.dashboard.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.types.DashboardWidget
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TimePeriod
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg
import org.expenny.core.ui.data.ui.AccountNameUi

data class State(
    val showAddRecordDialog: Boolean = false,
    val displayCurrency: String? = null,
    val selectAllAccounts: Boolean = true,
    val selectedAccounts: ImmutableList<AccountNameUi> = persistentListOf(),
    val accounts: ImmutableList<AccountNameUi> = persistentListOf(),
    val timePeriods: ImmutableList<TimePeriod> = persistentListOf(
        TimePeriod.Today,
        TimePeriod.ThisWeek,
        TimePeriod.ThisMonth,
        TimePeriod.ThisQuarter,
        TimePeriod.ThisYear
    ),
    val currentTimePeriod: TimePeriod = timePeriods[0],
    val widgets: ImmutableList<DashboardWidget> = DashboardWidget.values().toList().toImmutableList(),
    val balanceData: DashboardBalanceUi = DashboardBalanceUi(),
    val expensesData: DashboardExpensesUi = DashboardExpensesUi(),
)

sealed interface Action {
    class OnExpensesTimeSpanChange(val timePeriod: TimePeriod) : Action
    class OnCategoryExpensesSelect(val index: Int) : Action
    class OnWidgetClick(val widget: DashboardWidget) : Action
    class OnAccountSelect(val account: AccountNameUi) : Action
    class OnDisplayCurrencySelect(val id: Long) : Action
    class OnAddRecord(val recordType: RecordType) : Action
    object OnAddRecordClick : Action
    object OnAddRecordDialogDismiss : Action
    object OnShowMoreRecordsClick : Action
    object OnAllAccountsSelect : Action
    object OnCategoryExpensesDeselect : Action
    object OnDisplayCurrencyClick : Action
    object OnCreateRecordClick : Action
    object OnCreateAccountClick : Action
}

sealed interface Event {
    class NavigateToDisplayCurrencySelection(val selectedId: Long?) : Event
    class NavigateToRecords(val filter: RecordsListFilterNavArg? = null) : Event
    class NavigateToCreateRecord(val recordType: RecordType) : Event
    object NavigateToCreateAccount : Event
    object NavigateToAccounts : Event
}