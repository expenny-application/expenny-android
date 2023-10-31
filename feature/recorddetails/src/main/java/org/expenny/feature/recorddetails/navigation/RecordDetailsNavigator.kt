package org.expenny.feature.recorddetails.navigation

import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.core.ui.data.navargs.LongNavArg

interface RecordDetailsNavigator {
    fun navigateToAccountSelectionListScreen(selection: LongNavArg, excludeIds: LongArray?)
    fun navigateToCategorySelectionListScreen(selection: LongNavArg)
    fun navigateToLabelSelectionListScreen(selection: LongArrayNavArg)
    fun navigateBack()
}