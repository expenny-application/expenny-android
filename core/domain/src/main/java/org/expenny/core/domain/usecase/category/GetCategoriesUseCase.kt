package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.model.category.Category
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    operator fun invoke() : Flow<List<Category>> {
        return categoryRepository.getCategories()
    }
}