package org.expenny.feature.dashboard.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyBottomSheet
import org.expenny.core.ui.extensions.icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardAddRecordDialog(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    sheetState: SheetState,
    onRecordTypeSelect: (RecordType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismiss,
        actions = {
            RecordType.values().forEach { recordType ->
                Action(
                    icon = recordType.icon,
                    text = when (recordType) {
                        RecordType.Expense -> stringResource(R.string.add_expense_label)
                        RecordType.Income -> stringResource(R.string.add_income_label)
                        RecordType.Transfer -> stringResource(R.string.add_transfer_label)
                    },
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            onRecordTypeSelect(recordType)
                            onDismiss()
                        }
                    }
                )
            }
        }
    )
}
