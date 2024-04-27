package org.expenny.core.ui.extensions

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.common.types.AccountType
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.types.DashboardWidgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.PeriodType
import org.expenny.core.common.types.ProfileActionType
import org.expenny.core.common.types.RecordActionType
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.SortType
import org.expenny.core.resources.R

val AccountTrendType.label: String
    @Composable get() = stringArrayResource(R.array.trend_type)[ordinal]

val AccountType.label: String
    @Composable get() = stringResource(
        when (this) {
            AccountType.General -> R.string.general_label
            AccountType.Bank -> R.string.bank_label
            AccountType.Card -> R.string.card_label
            AccountType.Savings -> R.string.savings_label
            AccountType.Cash -> R.string.cash_label
        }
    )

val AccountType.icon: Painter
    @Composable get() = painterResource(
        when (this) {
            AccountType.General -> R.drawable.ic_wallet
            AccountType.Bank -> R.drawable.ic_bank
            AccountType.Card -> R.drawable.ic_card
            AccountType.Savings -> R.drawable.ic_safe
            AccountType.Cash -> R.drawable.ic_cash
        }
    )

val ApplicationLanguage.labelResId: Int
    @StringRes get() = when (this) {
        ApplicationLanguage.SystemDefault -> R.string.system_default_label
        ApplicationLanguage.English -> R.string.en_locale
        ApplicationLanguage.Belarusian -> R.string.be_locale
        ApplicationLanguage.Russian -> R.string.ru_locale
    }

val ApplicationLanguage.label: String
    @Composable get() = stringResource(labelResId)

val ApplicationTheme.labelResId: Int
    @StringRes get() = when (this) {
        ApplicationTheme.SystemDefault -> R.string.system_default_label
        ApplicationTheme.Dark -> R.string.dark_label
        ApplicationTheme.Light -> R.string.light_label
    }

val ApplicationTheme.label: String
    @Composable get() = stringResource(labelResId)

val DashboardWidgetType.label: String
    @Composable get() = stringResource(
        when (this) {
            DashboardWidgetType.Records -> R.string.records_label
            DashboardWidgetType.Accounts -> R.string.accounts_label
            DashboardWidgetType.Budgets -> R.string.budgets_label
            DashboardWidgetType.Analytics -> R.string.analytics_label
            DashboardWidgetType.Debts -> R.string.debts_label
        }
    )

val DashboardWidgetType.icon: Painter
    @Composable get() = painterResource(
        when (this) {
            DashboardWidgetType.Records -> R.drawable.ic_records
            DashboardWidgetType.Accounts -> R.drawable.ic_wallet
            DashboardWidgetType.Budgets -> R.drawable.ic_calculator
            DashboardWidgetType.Analytics -> R.drawable.ic_analytics
            DashboardWidgetType.Debts -> R.drawable.ic_debts
        }
    )

val IntervalType.labelResId: Int
    @ArrayRes get() = R.array.interval_type

val IntervalType.label: String
    @Composable get() = stringArrayResource(labelResId)[ordinal]

val PeriodType.label: String
    @Composable get() = stringArrayResource(R.array.period_type)[ordinal]

val RecordType.labelResId: Int
    @StringRes get() = when (this) {
        RecordType.Expense -> R.string.expense_label
        RecordType.Income -> R.string.income_label
        RecordType.Transfer -> R.string.transfer_label
    }

val RecordType.label: String
    @Composable get() = stringResource(labelResId)

val SortType.label: String
    @Composable get() = stringResource(
        when (this) {
            SortType.Date -> R.string.sort_by_date_label
            SortType.HighestAmount -> R.string.sort_by_highest_amount_label
            SortType.LowestAmount -> R.string.sort_by_lowest_amount_label
        }
    )

val RecordActionType.label: String
    @Composable get() = when (this) {
        RecordActionType.Select -> stringResource(R.string.select_label)
        RecordActionType.Clone -> stringResource(R.string.clone_label)
        RecordActionType.Edit -> stringResource(R.string.edit_label)
        RecordActionType.Delete -> stringResource(R.string.delete_label)
    }

val RecordActionType.icon: Painter
    @Composable get() = when (this) {
        RecordActionType.Select -> painterResource(R.drawable.ic_check)
        RecordActionType.Clone -> painterResource(R.drawable.ic_copy)
        RecordActionType.Edit -> painterResource(R.drawable.ic_edit)
        RecordActionType.Delete -> painterResource(R.drawable.ic_delete)
    }

val ProfileActionType.icon
    @Composable get() = when (this) {
        ProfileActionType.CreateProfile -> painterResource(R.drawable.ic_add_profile)
        ProfileActionType.SwitchProfile -> painterResource(R.drawable.is_select_profile)
        ProfileActionType.DeleteProfileData -> painterResource(R.drawable.ic_clear)
        ProfileActionType.DeleteProfile -> painterResource(R.drawable.ic_profile_delete)
    }

val ProfileActionType.label
    @Composable get() = when (this) {
        ProfileActionType.CreateProfile -> stringResource(R.string.create_new_profile_label)
        ProfileActionType.SwitchProfile -> stringResource(R.string.switch_profile_label)
        ProfileActionType.DeleteProfileData -> stringResource(R.string.clear_profile_data_label)
        ProfileActionType.DeleteProfile -> stringResource(R.string.delete_profile_label)
    }

val RecordType.icon
    @Composable get() = when (this) {
        RecordType.Expense -> painterResource(R.drawable.ic_expense)
        RecordType.Income -> painterResource(R.drawable.ic_income)
        RecordType.Transfer -> painterResource(R.drawable.ic_transfer)
    }