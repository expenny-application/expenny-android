package org.expenny.feature.categories.details.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDialog

@Composable
internal fun CategoryDetailsDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.delete_category_question_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.delete_associated_data_paragraph))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.delete_button),
                onClick = onConfirm
            )
        },
        leftButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        }
    )
}