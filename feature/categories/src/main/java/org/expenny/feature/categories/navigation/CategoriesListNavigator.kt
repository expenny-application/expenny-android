package org.expenny.feature.categories.navigation

interface CategoriesListNavigator {
    fun navigateToAddCategoryScreen()
    fun navigateToEditCategoryScreen(categoryId: Long)
    fun navigateBack()
}