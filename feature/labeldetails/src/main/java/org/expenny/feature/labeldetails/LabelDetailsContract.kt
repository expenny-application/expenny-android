package org.expenny.feature.labeldetails

import android.graphics.Color
import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_label_label),
    val enableSaveButton: Boolean = false,
    val showDeleteButton: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val nameInput: InputField = InputField(required = true),
    val colorInput: InputField = InputField(required = true, enabled = false),
    val colorArgb: Int = Color.parseColor("#FFE53935")
)

sealed interface Action {
    class OnNameChange(val name: String) : Action
    class OnColorChange(val colorArgb: Int) : Action
    object OnGenerateRandomColorClick : Action
    object OnDeleteDialogConfirm : Action
    object OnDeleteDialogDismiss : Action
    object OnSaveClick : Action
    object OnDeleteClick : Action
    object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    object NavigateBack : Event
}