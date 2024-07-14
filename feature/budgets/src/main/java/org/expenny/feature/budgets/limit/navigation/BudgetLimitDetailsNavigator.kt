package org.expenny.feature.budgets.limit.navigation

import org.expenny.core.ui.data.navargs.LongNavArg

interface BudgetLimitDetailsNavigator {
    fun navigateToCategorySelectionListScreen(selection: LongNavArg, excludeIds: LongArray?)
    fun navigateBack()
}