package org.expenny.feature.labels.navigation

interface LabelsListNavigator {
    fun navigateBack()
    fun navigateToCreateLabelScreen()
    fun navigateToEditLabelScreen(labelId: Long)
}