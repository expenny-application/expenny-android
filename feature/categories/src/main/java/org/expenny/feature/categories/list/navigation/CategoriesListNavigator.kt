package org.expenny.feature.categories.list.navigation

interface CategoriesListNavigator {
    fun navigateToAddCategoryScreen()
    fun navigateToEditCategoryScreen(categoryId: Long)
    fun navigateBack()
}