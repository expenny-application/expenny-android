package org.expenny.feature.settings.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyActionsBottomSheet
import org.expenny.core.ui.components.ExpennyActionsBottomSheetItem
import org.expenny.feature.settings.model.ProfileActionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsProfileActionsDialog(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    sheetState: SheetState,
    onActionTypeSelect: (ProfileActionType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyActionsBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismiss,
    ) {
        ProfileActionType.values().forEach { actionType ->
            ExpennyActionsBottomSheetItem(
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                        onActionTypeSelect(actionType)
                    }
                }
            ) {
                CompositionLocalProvider(LocalContentColor provides actionType.color) {
                    Icon(
                        painter = actionType.icon,
                        contentDescription = null
                    )
                    Text(text = actionType.label)
                }
            }
        }
    }
}
