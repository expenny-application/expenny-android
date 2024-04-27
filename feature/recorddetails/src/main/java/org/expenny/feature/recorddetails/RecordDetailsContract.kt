package org.expenny.feature.recorddetails

import android.net.Uri
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.toTimeString
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.recorddetails.model.LabelsInputField
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_record_label),
    val types: ImmutableList<RecordType> = RecordType.values().toList().toImmutableList(),
    val selectedType: RecordType = RecordType.Expense,
    val showDeleteButton: Boolean = false,
    val showAdditionsSection: Boolean = false,
    val showTransferDisclaimerButton: Boolean = false,
    val showTransferAmountInput: Boolean = false,
    val showCategoryInput: Boolean = false,
    val showTransferAccountInput: Boolean = false,
    val transferAmountCurrency: String = "",
    val amountCurrency: String = "",
    val amountInput: DecimalInputUi = DecimalInputUi(),
    val transferAmountInput: DecimalInputUi = DecimalInputUi(),
    val categoryInput: InputUi = InputUi(),
    val accountInput: InputUi = InputUi(),
    val transferAccountInput: InputUi = InputUi(),
    val dateInput: InputUi = InputUi(value = LocalDate.now().toDateString()),
    val timeInput: InputUi = InputUi(value = LocalTime.now().toTimeString()),
    val labelsInput: LabelsInputField = LabelsInputField(),
    val descriptionInput: InputUi = InputUi(isRequired = false),
    val receipts: List<Uri> = listOf(),
    val dialog: Dialog? = null
) {
    sealed interface Dialog {
        data object DatePickerDialog : Dialog
        data object TimePickerDialog : Dialog
        data object DeleteRecordDialog : Dialog
        data object ConversionDialog : Dialog
        data object ReceiptSourceDialog : Dialog
        data object ResetTransferDialog : Dialog
        data object TransferDisclaimerDialog : Dialog
    }
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToAccountSelectionList(val selection: LongNavArg, val excludeIds: LongArray? = null) : Event
    class NavigateToCategorySelectionList(val selection: LongNavArg) : Event
    class OpenCamera(val uri: Uri) : Event
    class OpenImageViewer(val uri: Uri) : Event
    data object RequestAmountInputFocus : Event
    data object OpenImagePicker : Event
    data object NavigateBack : Event
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnDateChange(val date: LocalDate) : Dialog
        class OnTimeChange(val time: LocalTime) : Dialog
        data object OnResetTransferDialogConfirm : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnReceiptSourceDialogCameraSelect : Dialog
        data object OnReceiptSourceDialogGallerySelect : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTypeChange(val type: RecordType) : Action
    class OnAmountChange(val amount: BigDecimal) : Action
    class OnTransferAmountChange(val amount: BigDecimal) : Action
    class OnCategorySelect(val selection: LongNavArg) : Action
    class OnAccountSelect(val selection: LongNavArg) : Action
    class OnLabelAdd(val label: String) : Action
    class OnLabelRemove(val index: Int) : Action
    class OnDescriptionChange(val description: String) : Action
    class OnLabelChange(val label: String) : Action
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : Action
    class OnReceiptSelect(val uri: Uri?) : Action
    class OnReceiptCapture(val uri: Uri?) : Action
    class OnDeleteReceiptClick(val uri: Uri) : Action
    class OnViewReceiptClick(val uri: Uri) : Action
    data object OnTransferDisclaimerClick : Action
    data object OnAddReceiptClick : Action
    data object OnSelectDateClick : Action
    data object OnSelectTimeClick : Action
    data object OnSelectCategoryClick : Action
    data object OnSelectAccountClick : Action
    data object OnSelectTransferAccountClick : Action
    data object OnSaveClick : Action
    data object OnDeleteClick : Action
    data object OnBackClick : Action
}