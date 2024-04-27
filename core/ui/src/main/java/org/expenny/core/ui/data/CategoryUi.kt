package org.expenny.core.ui.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.utils.Constants
import org.expenny.core.resources.R

data class CategoryUi(
    val id: Long,
    val name: String,
    val icon: IconUi
) {
    companion object {
        val unknown: CategoryUi
            @Composable get() = CategoryUi(
                id = Constants.NULL_ID,
                name = stringResource(R.string.unknown_label),
                icon = IconUi.unknown
            )
    }
}
