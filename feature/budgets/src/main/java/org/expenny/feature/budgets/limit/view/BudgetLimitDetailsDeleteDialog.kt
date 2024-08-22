package org.expenny.feature.budgets.limit.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDialog

@Composable
internal fun BudgetLimitDetailsDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.delete_budget_limit_question_label))
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