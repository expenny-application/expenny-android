package org.expenny.feature.recorddetails.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun RecordDetailsReceiptSourceDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.add_receipt_label))
        },
        content = {
            Text(text = stringResource(R.string.image_source_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(onClick = onGalleryClick) {
                Text(text = stringResource(R.string.gallery_button))
            }
        },
        dismissButton = {
            ExpennyTextButton(onClick = onCameraClick) {
                Text(text = stringResource(R.string.camera_button))
            }
        }
    )
}