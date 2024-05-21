package org.expenny.feature.records.list.navigation

interface RecordsListNavigator {
    fun navigateToEditRecordScreen(recordId: Long)
    fun navigateToCloneRecordScreen(recordId: Long)
    fun navigateToCreateRecordScreen()
    fun navigateBack()
}