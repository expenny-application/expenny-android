package org.expenny.core.domain.usecase.category

import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.model.category.CategoryUpdate
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(params: Params) {
        categoryRepository.updateCategory(
            CategoryUpdate(
                id = params.id,
                name = params.name,
                iconResName = params.iconResName,
                colorArgb = params.colorArgb
            )
        )
    }

    data class Params(
        val id: Long,
        val name: String,
        val iconResName: String,
        val colorArgb: Int
    )
}