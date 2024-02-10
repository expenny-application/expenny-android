package org.expenny.feature.dashboard.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
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
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onRecordTypeSelect(recordType)
                        onDismiss()
                    }
                }
            ) {
                Icon(
                    painter = when (recordType) {
                        RecordType.Expense -> painterResource(R.drawable.ic_expense)
                        RecordType.Income -> painterResource(R.drawable.ic_income)
                        RecordType.Transfer -> painterResource(R.drawable.ic_transfer)
                    },
                    contentDescription = null
                )
                Text(
                    text = when (recordType) {
                        RecordType.Expense -> stringResource(R.string.add_expense_label)
                        RecordType.Income -> stringResource(R.string.add_income_label)
                        RecordType.Transfer -> stringResource(R.string.add_transfer_label)
                    }
                )
            }
        }
    }
}
