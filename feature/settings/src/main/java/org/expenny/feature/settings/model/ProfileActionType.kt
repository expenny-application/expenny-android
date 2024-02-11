package org.expenny.feature.settings.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R

enum class ProfileActionType {
    ChangeProfile,
    DeleteProfileData,
    DeleteProfile;


    val color
        @Composable get() = when (this) {
            ChangeProfile -> MaterialTheme.colorScheme.onSurface
            DeleteProfileData -> MaterialTheme.colorScheme.error
            DeleteProfile -> MaterialTheme.colorScheme.error
        }

    val icon
        @Composable get() = when (this) {
            ChangeProfile -> painterResource(R.drawable.ic_profiles)
            DeleteProfileData -> painterResource(R.drawable.ic_clean)
            DeleteProfile -> painterResource(R.drawable.ic_profile_delete)
        }

    val label
        @Composable get() = when (this) {
            ChangeProfile -> stringResource(R.string.change_profile_label)
            DeleteProfileData -> stringResource(R.string.clear_profile_data_label)
            DeleteProfile -> stringResource(R.string.delete_profile_label)
        }
}