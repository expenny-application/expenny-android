package org.expenny.feature.records.details.navigation

import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.StringArrayNavArg

interface RecordDetailsNavigator {
    fun navigateToRecordLabelsListScreen(selection: StringArrayNavArg)
    fun navigateToAccountSelectionListScreen(selection: LongNavArg, excludeIds: LongArray?)
    fun navigateToCategorySelectionListScreen(selection: LongNavArg)
    fun navigateBack()
}