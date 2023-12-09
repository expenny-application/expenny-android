package org.expenny.core.domain.usecase.category

import org.expenny.core.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(id: Long) {
        categoryRepository.deleteCategory(id)
    }
}
