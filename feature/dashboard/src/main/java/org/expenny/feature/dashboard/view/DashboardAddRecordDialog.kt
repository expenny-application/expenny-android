package org.expenny.feature.dashboard.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.RecordType
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardAddRecordDialog(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    sheetState: SheetState,
    onRecordTypeSelect: (RecordType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyActionsBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismiss,
    ) {
        RecordType.values().forEach { recordType ->
            ExpennyActionsBottomSheetItem(
                label = when (recordType) {
                    RecordType.Expense -> stringResource(R.string.add_expense_label)
                    RecordType.Income -> stringResource(R.string.add_income_label)
                    RecordType.Transfer -> stringResource(R.string.add_transfer_label)
                },
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onRecordTypeSelect(recordType)
                    }
                }
            )
        }
    }
}
