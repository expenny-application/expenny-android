package org.expenny.core.ui.mapper

import androidx.compose.ui.graphics.Color
import org.expenny.core.common.utils.ResourceIdProvider
import org.expenny.core.model.category.Category
import org.expenny.core.ui.data.CategoryUi
import org.expenny.core.ui.data.IconUi
import org.expenny.core.resources.R
import javax.inject.Inject

class CategoryMapper @Inject constructor(
    private val resourceIdProvider: ResourceIdProvider,
) {

    operator fun invoke(model: Category): CategoryUi {
        return CategoryUi(
            id = model.id,
            name = model.name,
            icon = IconUi(
                iconResId = resourceIdProvider(model.iconResName) ?: R.drawable.ic_unknown,
                iconResName = model.iconResName,
                color = Color(model.colorArgb)
            )
        )
    }

    operator fun invoke(model: List<Category>): List<CategoryUi> {
        return model.map { invoke(it) }
    }
}