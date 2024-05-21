package org.expenny.feature.records.details.contract

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
import org.expenny.feature.records.details.model.LabelsInputField
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class RecordDetailsState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_record_label),
    val types: ImmutableList<RecordType> = RecordType.entries.toImmutableList(),
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

sealed interface RecordDetailsAction {
    sealed interface Dialog : RecordDetailsAction {
        class OnDateChange(val date: LocalDate) : Dialog
        class OnTimeChange(val time: LocalTime) : Dialog
        data object OnResetTransferDialogConfirm : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnReceiptSourceDialogCameraSelect : Dialog
        data object OnReceiptSourceDialogGallerySelect : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTypeChange(val type: RecordType) : RecordDetailsAction
    class OnAmountChange(val amount: BigDecimal) : RecordDetailsAction
    class OnTransferAmountChange(val amount: BigDecimal) : RecordDetailsAction
    class OnCategorySelect(val selection: LongNavArg) : RecordDetailsAction
    class OnAccountSelect(val selection: LongNavArg) : RecordDetailsAction
    class OnLabelAdd(val label: String) : RecordDetailsAction
    class OnLabelRemove(val index: Int) : RecordDetailsAction
    class OnDescriptionChange(val description: String) : RecordDetailsAction
    class OnLabelChange(val label: String) : RecordDetailsAction
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : RecordDetailsAction
    class OnReceiptSelect(val uri: Uri?) : RecordDetailsAction
    class OnReceiptCapture(val uri: Uri?) : RecordDetailsAction
    class OnDeleteReceiptClick(val uri: Uri) : RecordDetailsAction
    class OnViewReceiptClick(val uri: Uri) : RecordDetailsAction
    data object OnTransferDisclaimerClick : RecordDetailsAction
    data object OnAddReceiptClick : RecordDetailsAction
    data object OnSelectDateClick : RecordDetailsAction
    data object OnSelectTimeClick : RecordDetailsAction
    data object OnSelectCategoryClick : RecordDetailsAction
    data object OnSelectAccountClick : RecordDetailsAction
    data object OnSelectTransferAccountClick : RecordDetailsAction
    data object OnSaveClick : RecordDetailsAction
    data object OnDeleteClick : RecordDetailsAction
    data object OnBackClick : RecordDetailsAction
}

sealed interface RecordDetailsEvent {
    class ShowMessage(val message: StringResource) : RecordDetailsEvent
    class NavigateToAccountSelectionList(val selection: LongNavArg, val excludeIds: LongArray? = null) :
        RecordDetailsEvent
    class NavigateToCategorySelectionList(val selection: LongNavArg) : RecordDetailsEvent
    class OpenCamera(val uri: Uri) : RecordDetailsEvent
    class OpenImageViewer(val uri: Uri) : RecordDetailsEvent
    data object RequestAmountInputFocus : RecordDetailsEvent
    data object OpenImagePicker : RecordDetailsEvent
    data object NavigateBack : RecordDetailsEvent
}