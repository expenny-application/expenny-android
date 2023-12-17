package org.expenny.feature.settings.navigation

interface SettingsNavigator {
    fun navigateToCurrenciesListScreen()
    fun navigateToCreatePasscodeScreen()
    fun navigateToCategoriesListScreen()
    fun navigateBack()
}