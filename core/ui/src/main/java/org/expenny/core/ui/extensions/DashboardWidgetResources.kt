package org.expenny.core.ui.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.DashboardWidget
import org.expenny.core.resources.R

val DashboardWidget.iconResId: Int
    @DrawableRes
    get() = when (this) {
        DashboardWidget.Records -> R.drawable.ic_records_filled
        DashboardWidget.Accounts -> R.drawable.ic_accounts
        DashboardWidget.Budgets -> R.drawable.ic_budgets
        DashboardWidget.Analytics -> R.drawable.ic_analytics
        DashboardWidget.Debts -> R.drawable.ic_debts
    }

val DashboardWidget.labelResId: Int
    @StringRes
    get() = when (this) {
        DashboardWidget.Records -> R.string.records_label
        DashboardWidget.Accounts -> R.string.accounts_label
        DashboardWidget.Budgets -> R.string.budgets_label
        DashboardWidget.Analytics -> R.string.analytics_label
        DashboardWidget.Debts -> R.string.debts_label
    }

val DashboardWidget.label: String
    @Composable
    get() = stringResource(labelResId)

val DashboardWidget.icon: Painter
    @Composable
    get() = painterResource(iconResId)