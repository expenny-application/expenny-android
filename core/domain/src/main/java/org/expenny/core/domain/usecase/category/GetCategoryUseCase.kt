package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.model.category.Category
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(params: Params): Category? {
        return categoryRepository.getCategory(params.id).first()
    }

    data class Params(
        val id: Long
    )
}