package org.expenny.feature.settings.navigation

interface SettingsNavigator {
    fun navigateToCurrenciesListScreen()
    fun navigateToLabelsListScreen()
    fun navigateToCreatePasscodeScreen()
    fun navigateBack()
}