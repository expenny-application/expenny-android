package org.expenny.core.ui.mapper

import android.content.Context
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import org.expenny.core.model.category.Category
import org.expenny.core.ui.data.ui.CategoryUi
import org.expenny.core.ui.data.ui.IconUi
import org.expenny.core.ui.extensions.getDrawableResId
import org.expenny.core.resources.R
import javax.inject.Inject

class CategoryMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(model: Category): CategoryUi {
        return CategoryUi(
            id = model.id,
            name = model.name,
            icon = IconUi(
                iconResId = context.getDrawableResId(model.iconResName) ?: R.drawable.ic_unknown,
                color = Color(model.colorValue)
            )
        )
    }

    operator fun invoke(model: List<Category>): List<CategoryUi> {
        return model.map { invoke(it) }
    }
}