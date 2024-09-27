package org.expenny.feature.records.details.contract

import android.net.Uri
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.extensions.toDateTimeString
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.StringArrayNavArg
import java.math.BigDecimal
import java.time.LocalDateTime

data class RecordDetailsState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_record_label),
    val types: ImmutableList<RecordType>? = RecordType.entries.toImmutableList(),
    val selectedType: RecordType = RecordType.Expense,
    val showDeleteButton: Boolean = false,
    val showAdditionsSection: Boolean = false,
    val showConversionRateInput: Boolean = false,
    val showTransferDisclaimerButton: Boolean = false,
    val showTransferAccountInput: Boolean = false,
    val hideCategoryInput: Boolean = false,
    val amountCurrency: String? = null,
    val amountInput: DecimalInputUi = DecimalInputUi(isRequired = true),
    val convertedAmount: BigDecimal? = null,
    val convertedAmountCurrency: String? = null,
    val categoryInput: InputUi = InputUi(isRequired = true),
    val accountInput: InputUi = InputUi(isRequired = true),
    val transferAccountInput: InputUi = InputUi(isRequired = true),
    val conversionRateInput: DecimalInputUi = DecimalInputUi(isRequired = true),
    val dateTimeInput: InputUi = InputUi(value = LocalDateTime.now().toDateTimeString(), isRequired = true),
    val descriptionInput: InputUi = InputUi(),
    val labels: List<String> = emptyList(),
    val receipts: List<Uri> = emptyList(),
    val dialog: Dialog? = null,
) {
    sealed interface Dialog {
        data object DateTimePickerDialog : Dialog
        data object DeleteRecordDialog : Dialog
        data object ConversionDialog : Dialog
        data object ReceiptSourceDialog : Dialog
        data object ResetTransferDialog : Dialog
        data object TransferDisclaimerDialog : Dialog
    }
}

sealed interface RecordDetailsAction {
    sealed interface Dialog : RecordDetailsAction {
        class OnDateTimeChange(val datetime: LocalDateTime) : Dialog
        data object OnResetTransferDialogConfirm : Dialog
        data object OnDeleteRecordDialogConfirm : Dialog
        data object OnReceiptSourceDialogCameraSelect : Dialog
        data object OnReceiptSourceDialogGallerySelect : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTypeChange(val type: RecordType) : RecordDetailsAction
    class OnAmountChange(val amount: BigDecimal) : RecordDetailsAction
    class OnConversionRateChange(val rate: BigDecimal) : RecordDetailsAction
    class OnCategorySelect(val selection: LongNavArg) : RecordDetailsAction
    class OnAccountSelect(val selection: LongNavArg) : RecordDetailsAction
    class OnLabelSelect(val selection: StringArrayNavArg) : RecordDetailsAction
    class OnLabelRemove(val label: String) : RecordDetailsAction
    class OnDescriptionChange(val description: String) : RecordDetailsAction
    class OnAdditionsSectionVisibilityChange(val isVisible: Boolean) : RecordDetailsAction
    class OnReceiptSelect(val uri: Uri?) : RecordDetailsAction
    class OnReceiptCapture(val uri: Uri?) : RecordDetailsAction
    class OnDeleteReceiptClick(val uri: Uri) : RecordDetailsAction
    class OnViewReceiptClick(val uri: Uri) : RecordDetailsAction
    data object OnGrantCameraPermissions : RecordDetailsAction
    data object OnTransferDisclaimerClick : RecordDetailsAction
    data object OnAddReceiptClick : RecordDetailsAction
    data object OnSelectDateTimeClick : RecordDetailsAction
    data object OnSelectCategoryClick : RecordDetailsAction
    data object OnSelectAccountClick : RecordDetailsAction
    data object OnSelectTransferAccountClick : RecordDetailsAction
    data object OnSelectLabelsClick : RecordDetailsAction
    data object OnSaveClick : RecordDetailsAction
    data object OnDeleteClick : RecordDetailsAction
    data object OnBackClick : RecordDetailsAction
}

sealed interface RecordDetailsEvent {
    class ShowMessage(val message: StringResource) : RecordDetailsEvent
    class NavigateToAccountSelectionList(val selection: LongNavArg, val excludeIds: LongArray? = null) : RecordDetailsEvent
    class NavigateToCategorySelectionList(val selection: LongNavArg) : RecordDetailsEvent
    class NavigateToLabelsSelectionList(val selection: StringArrayNavArg) : RecordDetailsEvent
    class OpenCamera(val uri: Uri) : RecordDetailsEvent
    class OpenImageViewer(val uri: Uri) : RecordDetailsEvent
    data object CheckCameraPermissions : RecordDetailsEvent
    data object RequestAmountInputFocus : RecordDetailsEvent
    data object OpenImagePicker : RecordDetailsEvent
    data object NavigateBack : RecordDetailsEvent
}