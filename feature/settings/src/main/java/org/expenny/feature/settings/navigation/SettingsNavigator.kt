package org.expenny.feature.settings.navigation

interface SettingsNavigator {
    fun navigateToCreateProfileScreen()
    fun navigateToCurrenciesListScreen()
    fun navigateToCreatePasscodeScreen()
    fun navigateToCategoriesListScreen()
    fun restartApplication(isDataCleanupRequested: Boolean)
    fun navigateBack()
}