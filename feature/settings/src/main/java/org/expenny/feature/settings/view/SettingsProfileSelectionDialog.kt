package org.expenny.feature.settings.view

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.expenny.core.resources.R
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.ui.ProfileUi
import org.expenny.core.ui.foundation.ExpennyListDialog
import org.expenny.core.ui.foundation.ExpennyListDialogItem
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun SettingsProfileSelectionDialog(
    modifier: Modifier = Modifier,
    profiles: List<ProfileUi>,
    selectedProfile: ProfileUi?,
    onSelect: (Long) -> Unit,
    onCreate: () -> Unit,
    onDismiss: () -> Unit,
) {
    ExpennyListDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_profile_label))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onCreate,
                content = {
                    Text(text = stringResource(R.string.create_profile_button))
                }
            )
        },
        listContent = {
            items(profiles) { profile ->
                ExpennyListDialogItem(
                    selectionType = SelectionType.Single,
                    isSelected = selectedProfile?.id == profile.id,
                    onClick = {
                        onSelect(profile.id)
                    }
                ) {
                    Text(
                        text = profile.displayName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    )
}