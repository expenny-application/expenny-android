package org.expenny.feature.getstarted.navigation

interface GetStartedNavigator {
    fun navigateToHome()
    fun navigateToCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}