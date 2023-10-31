package org.expenny.feature.records.navigation

interface RecordsListNavigator {
    fun navigateToEditRecordScreen(recordId: Long)
    fun navigateToCloneRecordScreen(recordId: Long)
    fun navigateToCreateRecordScreen()
    fun navigateBack()
}