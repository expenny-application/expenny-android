package org.expenny.core.ui.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.expenny.core.resources.R

data class IconUi(
    val iconResId: Int,
    val iconResName: String,
    val color: Color,
) {
    companion object {
        val transfer: IconUi
            @Composable get() = IconUi(
                iconResId = R.drawable.ic_transfer,
                iconResName = "ic_local_transfer",
                color = MaterialTheme.colorScheme.primary
            )
        val unknown: IconUi
            @Composable get() = IconUi(
                iconResId = R.drawable.ic_unknown,
                iconResName = "ic_unknown",
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    }
}
