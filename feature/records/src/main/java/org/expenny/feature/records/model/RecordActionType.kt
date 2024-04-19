package org.expenny.feature.records.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R

enum class RecordActionType {
    Select, Clone, Edit, Delete;


    val label: String
        @Composable
        get() = when (this) {
            Select -> stringResource(R.string.select_label)
            Clone -> stringResource(R.string.clone_label)
            Edit -> stringResource(R.string.edit_label)
            Delete -> stringResource(R.string.delete_label)
        }

    val icon: Painter
        @Composable
        get() = when (this) {
            Select -> painterResource(R.drawable.ic_check)
            Clone -> painterResource(R.drawable.ic_copy)
            Edit -> painterResource(R.drawable.ic_edit)
            Delete -> painterResource(R.drawable.ic_delete)
        }
}