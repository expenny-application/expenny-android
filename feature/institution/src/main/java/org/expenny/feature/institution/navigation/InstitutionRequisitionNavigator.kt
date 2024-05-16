package org.expenny.feature.institution.navigation

interface InstitutionRequisitionNavigator {
    fun navigateToInstitutionAccountsPreviewScreen(requisitionId: String)
    fun navigateBackToAccountsListScreen()
    fun navigateBack()
}