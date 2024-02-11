package org.expenny.feature.settings.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R

enum class ProfileActionType {
    CreateProfile,
    SwitchProfile,
    DeleteProfileData,
    DeleteProfile;

    val color
        @Composable get() = when (this) {
            CreateProfile -> MaterialTheme.colorScheme.onSurface
            SwitchProfile -> MaterialTheme.colorScheme.onSurface
            DeleteProfileData -> MaterialTheme.colorScheme.error
            DeleteProfile -> MaterialTheme.colorScheme.error
        }

    val icon
        @Composable get() = when (this) {
            CreateProfile -> painterResource(R.drawable.ic_add_profile)
            SwitchProfile -> painterResource(R.drawable.is_select_profile)
            DeleteProfileData -> painterResource(R.drawable.ic_clear)
            DeleteProfile -> painterResource(R.drawable.ic_profile_delete)
        }

    val label
        @Composable get() = when (this) {
            CreateProfile -> stringResource(R.string.create_new_profile_label)
            SwitchProfile -> stringResource(R.string.switch_profile_label)
            DeleteProfileData -> stringResource(R.string.clear_profile_data_label)
            DeleteProfile -> stringResource(R.string.delete_profile_label)
        }
}