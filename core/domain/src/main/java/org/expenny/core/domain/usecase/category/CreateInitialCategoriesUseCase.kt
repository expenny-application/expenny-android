package org.expenny.core.domain.usecase.category

import android.graphics.Color
import org.expenny.core.common.models.StringResource.Companion.fromArrayRes
import org.expenny.core.common.utils.StringResourceProvider
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.resources.R
import javax.inject.Inject

class CreateInitialCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val provideString: StringResourceProvider,
) {

    suspend operator fun invoke(params: Params) {
        val profileId = params.profileId

        categoryRepository.createCategories(
            listOf(
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 0)), "ic_cart", Color.parseColor("#FFE53935")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 1)), "ic_dinner", Color.parseColor("#FF723CEB")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 2)), "ic_house", Color.parseColor("#FFFFD600")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 3)), "ic_bag", Color.parseColor("#FF42A5F5")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 4)), "ic_family", Color.parseColor("#FFEF6C00")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 5)), "ic_suitcase", Color.parseColor("#FFE040FB")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 6)), "ic_care", Color.parseColor("#FFEC407A")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 7)), "ic_study", Color.parseColor("#FF2962FF")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 8)), "ic_savings", Color.parseColor("#FF00BFA5")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 9)), "ic_bill", Color.parseColor("#FF8D6E63")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 10)), "ic_bus", Color.parseColor("#FFC0CA33")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 11)), "ic_money", Color.parseColor("#FF41C300")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 12)), "ic_cheque", Color.parseColor("#FF616161")),
            )
        )
    }

    data class Params(
        val profileId: Long
    )
}