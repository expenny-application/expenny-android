package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.category.CategoryCreate
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(params: Params) {
        val profileId = getCurrentProfile().first()!!.id

        categoryRepository.createCategory(
            CategoryCreate(
                profileId = profileId,
                name = params.name,
                colorArgb = params.colorArgb,
                iconResName = params.iconResName
            )
        )
    }

    data class Params(
        val name: String,
        val colorArgb: Int,
        val iconResName: String
    )
}