package org.expenny.feature.records.details.navigation

import org.expenny.core.ui.data.navargs.LongNavArg

interface RecordDetailsNavigator {
    fun navigateToAccountSelectionListScreen(selection: LongNavArg, excludeIds: LongArray?)
    fun navigateToCategorySelectionListScreen(selection: LongNavArg)
    fun navigateBack()
}