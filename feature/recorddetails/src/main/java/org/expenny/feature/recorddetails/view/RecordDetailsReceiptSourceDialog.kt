package org.expenny.feature.recorddetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@Composable
internal fun RecordDetailsReceiptSourceDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.add_receipt_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.image_source_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                onClick = onGalleryClick,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.gallery_button)
                )
            )
        },
        dismissButton = {
            ExpennyButton(
                onClick = onCameraClick,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.camera_button)
                )
            )
        }
    )
}