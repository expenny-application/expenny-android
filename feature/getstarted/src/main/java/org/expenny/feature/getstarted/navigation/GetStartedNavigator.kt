package org.expenny.feature.getstarted.navigation

interface GetStartedNavigator {
    fun navigateToApp()
    fun navigateToCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}