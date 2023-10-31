package org.expenny.feature.recorddetails

import android.net.Uri
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.toTimeString
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.ui.LabelUi
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_record_label),
    val types: ImmutableList<RecordType> = RecordType.values().toList().toImmutableList(),
    val selectedType: RecordType = RecordType.Expense,
    val showDeleteButton: Boolean = false,
    val showAdditionsSection: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showAmountConversionDialog: Boolean = false,
    val showDeleteReceiptDialog: Boolean = false,
    val showReceiptSourceDialog: Boolean = false,
    val showResetTransferDialog: Boolean = false,
    val showTransferDisclaimer: Boolean = false,
    val showTransferAmountInput: Boolean = false,
    val showCategoryInput: Boolean = false,
    val showTransferAccountInput: Boolean = false,
    val transferAmountCurrency: String = "",
    val amountCurrency: String = "",
    val amountInput: MonetaryInputField = MonetaryInputField(),
    val transferAmountInput: MonetaryInputField = MonetaryInputField(),
    val categoryInput: InputField = InputField(),
    val accountInput: InputField = InputField(),
    val transferAccountInput: InputField = InputField(),
    val dateInput: InputField = InputField(value = LocalDate.now().toDateString()),
    val timeInput: InputField = InputField(value = LocalTime.now().toTimeString()),
    val payeeOrPayerInput: InputField = InputField(required = false),
    val descriptionInput: InputField = InputField(required = false),
    val labels: List<LabelUi> = listOf(),
    val receipts: List<Uri> = listOf(),
)

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    class NavigateToAccountSelectionList(val selection: LongNavArg, val excludeIds: LongArray? = null) : Event
    class NavigateToCategorySelectionList(val selection: LongNavArg) : Event
    class NavigateToLabelSelectionList(val selection: LongArrayNavArg) : Event
    class OpenCamera(val uri: Uri) : Event
    class OpenImageViewer(val uri: Uri) : Event
    data object RequestAmountInputFocus : Event
    data object OpenImagePicker : Event
    data object NavigateBack : Event
}

sealed interface Action {
    class OnTypeChange(val type: RecordType) : Action
    class OnAmountChange(val amount: BigDecimal) : Action
    class OnTransferAmountChange(val amount: BigDecimal) : Action
    class OnCategorySelect(val selection: LongNavArg) : Action
    class OnAccountSelect(val selection: LongNavArg) : Action
    class OnLabelsSelect(val selection: LongArrayNavArg) : Action
    class OnDateChange(val date: LocalDate) : Action
    class OnTimeChange(val time: LocalTime) : Action
    class OnPayeeOrPayerChange(val payeeOrPayer: String) : Action
    class OnDescriptionChange(val description: String) : Action
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : Action
    class OnDeleteLabelClick(val id: Long) : Action
    class OnReceiptSelect(val uri: Uri?) : Action
    class OnReceiptCapture(val uri: Uri?) : Action
    class OnDeleteReceiptClick(val uri: Uri) : Action
    class OnViewReceiptClick(val uri: Uri) : Action
    data object OnAddReceiptClick : Action
    data object OnSelectDateClick : Action
    data object OnSelectTimeClick : Action
    data object OnSelectCategoryClick : Action
    data object OnSelectAccountClick : Action
    data object OnSelectTransferAccountClick : Action
    data object OnSelectLabelClick : Action
    data object OnResetTransferDialogConfirm : Action
    data object OnDeleteRecordDialogConfirm : Action
    data object OnReceiptSourceDialogCameraSelect : Action
    data object OnReceiptSourceDialogGallerySelect : Action
    data object OnDialogDismiss : Action
    data object OnSaveClick : Action
    data object OnDeleteClick : Action
    data object OnBackClick : Action
}