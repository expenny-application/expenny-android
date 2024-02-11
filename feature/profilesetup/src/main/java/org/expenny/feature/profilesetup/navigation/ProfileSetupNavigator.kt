package org.expenny.feature.profilesetup.navigation

interface ProfileSetupNavigator {
    fun navigateToHome()
    fun navigateToCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}