package org.expenny.feature.records.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.expenny.core.common.extensions.conversionRateOf
import org.expenny.core.common.extensions.convertBy
import org.expenny.core.common.extensions.setScaleRoundingDown
import org.expenny.core.common.extensions.toDateTimeString
import org.expenny.core.common.extensions.toLocalDateTime
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.utils.Constants.NULL_ID
import org.expenny.core.common.utils.ExternalFilesDirectoryHandler
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.account.GetAccountUseCase
import org.expenny.core.domain.usecase.account.GetLastUsedAccountUseCase
import org.expenny.core.domain.usecase.category.GetCategoryUseCase
import org.expenny.core.domain.usecase.category.GetMostUsedCategoryUseCase
import org.expenny.core.domain.usecase.record.CreateRecordUseCase
import org.expenny.core.domain.usecase.record.DeleteRecordUseCase
import org.expenny.core.domain.usecase.record.GetRecordUseCase
import org.expenny.core.domain.usecase.record.UpdateRecordUseCase
import org.expenny.core.domain.validators.AlphanumericValidator
import org.expenny.core.domain.validators.RequiredBigDecimalValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.domain.validators.ValidationResult
import org.expenny.core.model.account.Account
import org.expenny.core.model.category.Category
import org.expenny.core.model.record.Record
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.StringArrayNavArg
import org.expenny.feature.records.details.contract.RecordDetailsAction
import org.expenny.feature.records.details.contract.RecordDetailsEvent
import org.expenny.feature.records.details.contract.RecordDetailsState
import org.expenny.feature.records.details.navigation.RecordDetailsNavArgs
import org.expenny.feature.records.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RecordDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val validateInput: ValidateInputUseCase,
    private val createRecord: CreateRecordUseCase,
    private val updateRecord: UpdateRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val getRecord: GetRecordUseCase,
    private val getAccount: GetAccountUseCase,
    private val getCategory: GetCategoryUseCase,
    private val getLastCreatedAccount: GetLastUsedAccountUseCase,
    private val getLastUsedCategory: GetMostUsedCategoryUseCase,
    private val filesDirectoryHandler: ExternalFilesDirectoryHandler,
) : ExpennyViewModel<RecordDetailsAction>(), ContainerHost<RecordDetailsState, RecordDetailsEvent> {

    private val maxReceiptsCount = 3
    private val accountSelectionResultCode = 1
    private val transferAccountSelectionResultCode = 2
    private var selectedCategory: Category? = null
    private var selectedAccount: Account? = null
    private var selectedTransferAccount: Account? = null
    private var currentRecord: Record? = null

    override val container = container<RecordDetailsState, RecordDetailsEvent>(
        initialState = RecordDetailsState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setInitialData()
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: RecordDetailsAction) {
        when (action) {
            is RecordDetailsAction.OnTypeChange -> handleOnTypeChange(action)
            is RecordDetailsAction.OnAmountChange -> handleOnAmountChange(action)
            is RecordDetailsAction.OnConversionRateChange -> handleOnConversionRateChange(action)
            is RecordDetailsAction.OnDescriptionChange -> handleOnDescriptionChange(action)
            is RecordDetailsAction.OnAdditionsSectionVisibilityChange -> handleOnAdditionsSectionVisibilityChange(action)
            is RecordDetailsAction.OnCategorySelect -> handleOnCategorySelect(action)
            is RecordDetailsAction.OnAccountSelect -> handleOnAccountSelect(action)
            is RecordDetailsAction.OnLabelRemove -> handleOnLabelRemove(action)
            is RecordDetailsAction.OnSelectLabelsClick -> handleOnSelectLabelsClick()
            is RecordDetailsAction.OnSelectCategoryClick -> handleOnSelectCategoryClick()
            is RecordDetailsAction.OnSelectAccountClick -> handleOnSelectAccountClick()
            is RecordDetailsAction.OnSelectTransferAccountClick -> handleOnSelectTransferAccountClick()
            is RecordDetailsAction.OnSelectDateTimeClick -> handleOnSelectDateTimeClick()
            is RecordDetailsAction.OnAddReceiptClick -> handleOnAddReceiptClick()
            is RecordDetailsAction.OnLabelSelect -> handleOnLabelSelect(action)
            is RecordDetailsAction.OnReceiptSelect -> handleOnReceiptSelect(action)
            is RecordDetailsAction.OnReceiptCapture -> handleOnReceiptCapture(action)
            is RecordDetailsAction.OnDeleteReceiptClick -> handleOnDeleteReceiptClick(action)
            is RecordDetailsAction.OnViewReceiptClick -> handleOnViewReceiptClick(action)
            is RecordDetailsAction.OnTransferDisclaimerClick -> handleOnTransferDisclaimerClick()
            is RecordDetailsAction.OnBackClick -> handleOnBackClick()
            is RecordDetailsAction.OnSaveClick -> handleOnSaveClick()
            is RecordDetailsAction.OnDeleteClick -> handleOnDeleteClick()
            is RecordDetailsAction.OnGrantCameraPermissions -> handleOnGrantCameraPermissions()
            is RecordDetailsAction.Dialog.OnDateTimeChange -> handleOnDateTimeChange(action)
            is RecordDetailsAction.Dialog.OnReceiptSourceDialogCameraSelect -> handleOnReceiptSourceDialogCameraSelect()
            is RecordDetailsAction.Dialog.OnReceiptSourceDialogGallerySelect -> handleOnReceiptSourceDialogGallerySelect()
            is RecordDetailsAction.Dialog.OnDeleteRecordDialogConfirm -> handleOnDeleteRecordDialogConfirm()
            is RecordDetailsAction.Dialog.OnResetTransferDialogConfirm -> handleOnResetTransferDialogConfirm()
            is RecordDetailsAction.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(RecordDetailsEvent.ShowMessage(message.text))
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(RecordDetailsEvent.NavigateBack)
    }

    private fun handleOnLabelRemove(action: RecordDetailsAction.OnLabelRemove) = intent {
        reduce {
            state.copy(
                labels = state.labels - action.label
            )
        }
    }

    private fun handleOnReceiptSourceDialogCameraSelect() = intent {
        dismissDialog()
        postSideEffect(RecordDetailsEvent.CheckCameraPermissions)
    }

    private fun handleOnGrantCameraPermissions() = intent {
        val uri = filesDirectoryHandler.getPersistentImageUri()
        postSideEffect(RecordDetailsEvent.OpenCamera(uri))
    }

    private fun handleOnReceiptSourceDialogGallerySelect() = intent {
        dismissDialog()
        postSideEffect(RecordDetailsEvent.OpenImagePicker)
    }

    private fun handleOnCategorySelect(action: RecordDetailsAction.OnCategorySelect) = intent {
        getCategory(GetCategoryUseCase.Params(action.selection.value))!!.also { category ->
            setCategory(category)
        }
    }

    private fun handleOnAccountSelect(action: RecordDetailsAction.OnAccountSelect) = intent {
        val account = getAccount(GetAccountUseCase.Params(action.selection.value)).first()!!

        when (action.selection.resultCode) {
            accountSelectionResultCode -> setAccount(account)
            transferAccountSelectionResultCode -> setTransferAccount(account)
        }

        if (selectedAccountAndTransferAccountCurrenciesNotEqual() && state.selectedType == RecordType.Transfer) {
            val conversionRate = getAccountAndTransferAccountCurrenciesConversionRate()
            val convertedAmount = convertCurrentAmountByRate(conversionRate)
            val convertedAmountCurrency = selectedTransferAccount?.currency?.unit?.code

            showAndUpdateConversionData(
                conversionRate = conversionRate,
                convertedAmount = convertedAmount,
                convertedAmountCurrency = convertedAmountCurrency
            )
        } else {
            hideAndClearConversionData()
        }
    }

    private fun handleOnAmountChange(action: RecordDetailsAction.OnAmountChange) = blockingIntent {
        reduce {
            state.copy(
                amountInput = state.amountInput.copy(
                    value = action.amount,
                    error = validateRequiredInput(action.amount).errorRes
                )
            )
        }
        if (state.convertedAmount != null) {
            reduce {
                state.copy(convertedAmount = action.amount.convertBy(state.conversionRateInput.value))
            }
        }
    }

    private fun handleOnConversionRateChange(action: RecordDetailsAction.OnConversionRateChange) = blockingIntent {
        reduce {
            state.copy(
                conversionRateInput = state.conversionRateInput.copy(
                    value = action.rate,
                    error = validateRequiredInput(action.rate).errorRes
                ),
                convertedAmount = state.amountInput.value.convertBy(action.rate),
            )
        }
    }

    private fun handleOnSelectLabelsClick() = intent {
        postSideEffect(RecordDetailsEvent.NavigateToLabelsSelectionList(StringArrayNavArg(state.labels.toTypedArray())))
    }

    private fun handleOnSelectCategoryClick() = intent {
        val selection = LongNavArg(selectedCategory?.id ?: NULL_ID)
        postSideEffect(RecordDetailsEvent.NavigateToCategorySelectionList(selection))
    }

    private fun handleOnSelectAccountClick() = intent {
        val selection = getAccountSelection()
        postSideEffect(RecordDetailsEvent.NavigateToAccountSelectionList(selection))
    }

    private fun handleOnSelectTransferAccountClick() = intent {
        val selection = getTransferAccountSelection()
        val excludeIds = selectedAccount?.id?.let { longArrayOf(it) }
        postSideEffect(RecordDetailsEvent.NavigateToAccountSelectionList(selection, excludeIds))
    }

    private fun handleOnDateTimeChange(action: RecordDetailsAction.Dialog.OnDateTimeChange) = intent {
        reduce {
            state.copy(dateTimeInput = state.dateTimeInput.copy(value = action.datetime.toDateTimeString()))
        }
    }

    private fun handleOnDescriptionChange(action: RecordDetailsAction.OnDescriptionChange) = blockingIntent {
        reduce {
            state.copy(descriptionInput = state.descriptionInput.copy(value = action.description))
        }
    }

    private fun handleOnAdditionsSectionVisibilityChange(action: RecordDetailsAction.OnAdditionsSectionVisibilityChange) = intent {
        reduce {
            state.copy(showAdditionsSection = action.isVisible)
        }
    }

    private fun handleOnAddReceiptClick() = intent {
        if (state.receipts.size < maxReceiptsCount) {
            reduce {
                state.copy(dialog = RecordDetailsState.Dialog.ReceiptSourceDialog)
            }
        } else {
            postSideEffect(RecordDetailsEvent.ShowMessage(fromRes(R.string.max_photos_amount_error, maxReceiptsCount)))
        }
    }

    private fun handleOnLabelSelect(action: RecordDetailsAction.OnLabelSelect) = intent {
        reduce {
            state.copy(labels = action.selection.values.toList())
        }
    }

    private fun handleOnReceiptSelect(action: RecordDetailsAction.OnReceiptSelect) = intent {
        if (action.uri != null) {
            val uri = filesDirectoryHandler.copyFileToPersistentDirectory(action.uri)
            reduce {
                state.copy(receipts = state.receipts + uri!!)
            }
        }
    }

    private fun handleOnReceiptCapture(action: RecordDetailsAction.OnReceiptCapture) = intent {
        if (action.uri != null) {
            reduce {
                state.copy(receipts = state.receipts + action.uri)
            }
        }
    }

    private fun handleOnDeleteReceiptClick(action: RecordDetailsAction.OnDeleteReceiptClick) = intent {
        reduce {
            state.copy(receipts = state.receipts - action.uri)
        }
    }

    private fun handleOnTransferDisclaimerClick() = intent {
        reduce { state.copy(dialog = RecordDetailsState.Dialog.TransferDisclaimerDialog) }
    }

    private fun handleOnViewReceiptClick(action: RecordDetailsAction.OnViewReceiptClick) = intent {
        postSideEffect(RecordDetailsEvent.OpenImageViewer(action.uri))
    }

    private fun handleOnTypeChange(action: RecordDetailsAction.OnTypeChange) = intent {
        setRecordType(action.type)
    }

    private fun selectedAccountAndTransferAccountCurrenciesNotEqual(): Boolean {
        val currency = selectedAccount?.currency
        val transferCurrency = selectedTransferAccount?.currency

        return currency?.id != null && transferCurrency?.id != null && currency.id != transferCurrency.id
    }

    private fun getAccountAndTransferAccountCurrenciesConversionRate(): BigDecimal {
        val currency = selectedAccount?.currency
        val transferCurrency = selectedTransferAccount?.currency

        return if (transferCurrency != null && currency != null) {
            transferCurrency.baseToQuoteRate.conversionRateOf(currency.baseToQuoteRate)
        } else BigDecimal.ONE
    }

    private fun convertCurrentAmountByRate(rate: BigDecimal): BigDecimal {
        return state.amountInput.value.convertBy(rate)
    }

    private fun showAndUpdateConversionData(
        conversionRate: BigDecimal,
        convertedAmount: BigDecimal,
        convertedAmountCurrency: String?,
    ) = intent {
        reduce {
            state.copy(
                showConversionRateInput = true,
                convertedAmount = convertedAmount,
                convertedAmountCurrency = convertedAmountCurrency,
                conversionRateInput = state.conversionRateInput.copy(
                    value = conversionRate,
                    error = validateRequiredInput(conversionRate).errorRes
                )
            )
        }
    }

    private fun hideAndClearConversionData() = intent {
        reduce {
            state.copy(
                showConversionRateInput = false,
                convertedAmount = null,
                convertedAmountCurrency = null,
                conversionRateInput = state.conversionRateInput.copy(
                    value = BigDecimal.ZERO,
                    error = null
                )
            )
        }
    }

    private fun handleOnSaveClick() {
        if (validateInputs()) {
            intent {
                val transferAmount = when (state.selectedType) {
                    RecordType.Transfer -> state.convertedAmount
                    else -> null
                }

                if (currentRecord == null) {
                    createRecord(
                        CreateRecordUseCase.Params(
                            type = state.selectedType,
                            accountId = selectedAccount!!.id,
                            transferAccountId = selectedTransferAccount?.id,
                            categoryId = selectedCategory?.id,
                            receiptsUris = state.receipts,
                            description = state.descriptionInput.value,
                            labels = state.labels,
                            amount = state.amountInput.value,
                            transferAmount = transferAmount,
                            date = state.dateTimeInput.value.toLocalDateTime() ?: LocalDateTime.now()
                        )
                    )
                } else {
                    updateRecord(
                        UpdateRecordUseCase.Params(
                            id = currentRecord!!.id,
                            type = state.selectedType,
                            accountId = selectedAccount!!.id,
                            transferAccountId = selectedTransferAccount?.id,
                            categoryId = selectedCategory?.id,
                            receiptsUris = state.receipts,
                            description = state.descriptionInput.value,
                            labels = state.labels,
                            amount = state.amountInput.value,
                            transferAmount = transferAmount,
                            date = state.dateTimeInput.value.toLocalDateTime() ?: LocalDateTime.now()
                        )
                    )
                }
                postSideEffect(RecordDetailsEvent.ShowMessage(fromRes(R.string.saved_message)))
                postSideEffect(RecordDetailsEvent.NavigateBack)
            }
        }
    }

    private fun handleOnSelectDateTimeClick() = intent {
        reduce { state.copy(dialog = RecordDetailsState.Dialog.DateTimePickerDialog) }
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(dialog = RecordDetailsState.Dialog.DeleteRecordDialog) }
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        dismissDialog()
        deleteRecord(currentRecord!!.id)
        postSideEffect(RecordDetailsEvent.ShowMessage(fromRes(R.string.deleted_message)))
        postSideEffect(RecordDetailsEvent.NavigateBack)
    }

    private fun handleOnResetTransferDialogConfirm() = intent {

    }

    private fun handleOnDialogDismiss() = intent {
        dismissDialog()
    }

    private fun validateAlphanumericInput(value: String) = validateInput(
        value, listOf(AlphanumericValidator())
    )

    private fun validateRequiredInput(value: BigDecimal) = validateInput(
        value.toPlainString(), listOf(RequiredBigDecimalValidator())
    )

    private fun validateRequiredInput(value: String) = validateInput(
        value, listOf(RequiredStringValidator())
    )

    private fun validateInputs(): Boolean {
        var categoryInputResult: ValidationResult? = null
        var transferAccountInputResult: ValidationResult? = null
        var conversionRateInputResult: ValidationResult? = null
        val amountInputResult: ValidationResult = validateRequiredInput(state.amountInput.value)
        val accountInputResult: ValidationResult = validateRequiredInput(state.accountInput.value)

        if (!state.hideCategoryInput) {
            categoryInputResult = validateRequiredInput(state.categoryInput.value)
        }

        if (state.showTransferAccountInput) {
            transferAccountInputResult = validateRequiredInput(state.transferAccountInput.value)
        }

        if (state.showConversionRateInput) {
            conversionRateInputResult = validateRequiredInput(state.conversionRateInput.value)
        }

        intent {
            reduce {
                state.copy(
                    amountInput = state.amountInput.copy(error = amountInputResult.errorRes),
                    accountInput = state.accountInput.copy(error = accountInputResult.errorRes),
                    transferAccountInput = state.transferAccountInput.copy(error = transferAccountInputResult?.errorRes),
                    categoryInput = state.categoryInput.copy(error = categoryInputResult?.errorRes),
                    conversionRateInput = state.conversionRateInput.copy(error = conversionRateInputResult?.errorRes),
                )
            }
        }

        return listOf(
            amountInputResult,
            accountInputResult,
            categoryInputResult,
            transferAccountInputResult,
            conversionRateInputResult
        ).all { it?.isValid ?: true }
    }

    private fun setInitialData() {
        savedStateHandle.navArgs<RecordDetailsNavArgs>().also { args ->
            intent {
                if (args.recordId != null) {
                    val record = getRecord(GetRecordUseCase.Params(args.recordId)).first()

                    if (record != null) {
                        setInitialStateData(record)
                    } else {
                        postSideEffect(RecordDetailsEvent.ShowMessage(fromRes(R.string.invalid_argument_error)))
                        postSideEffect(RecordDetailsEvent.NavigateBack)
                    }
                } else {
                    getLastCreatedAccount()?.also {
                        setAccount(it)
                    }

                    if (args.recordType != RecordType.Transfer) {
                        getLastUsedCategory()?.also {
                            setCategory(it)
                        }
                    }

                    setRecordType(args.recordType)

                    delay(200)
                    postSideEffect(RecordDetailsEvent.RequestAmountInputFocus)
                }
            }
        }
    }

    private fun setInitialStateData(record: Record) = intent {
        currentRecord = record
        selectedAccount = record.account
        selectedTransferAccount = (record as? Record.Transfer)?.transferAccount
        selectedCategory = (record as? Record.Transaction)?.category

        val showTransferDisclaimerButton = record is Record.Transfer
        val showTransferAccountInput = record is Record.Transfer
        val hideCategoryInput = record is Record.Transfer

        if (record is Record.Transfer) {
            setTransferAccount(record.transferAccount)
        }

        if (record is Record.Transaction && record.category != null) {
            setCategory(record.category!!)
        }

        reduce {
            state.copy(
                showDeleteButton = true,
                showTransferAccountInput = showTransferAccountInput,
                showTransferDisclaimerButton = showTransferDisclaimerButton,
                hideCategoryInput = hideCategoryInput,
                toolbarTitle = fromRes(R.string.edit_record_label),
                selectedType = record.recordType,
                receipts = record.attachments,
                labels = record.labels,
                amountInput = state.amountInput.copy(value = record.amount.value, isEnabled = true),
                dateTimeInput = state.dateTimeInput.copy(value = record.date.toDateTimeString()),
                descriptionInput = state.descriptionInput.copy(value = record.description),
                accountInput = state.accountInput.copy(value = record.account.displayName),
            )
        }

        if (selectedAccountAndTransferAccountCurrenciesNotEqual() && record is Record.Transfer) {
            val conversionRate = record.transferAmount.value.conversionRateOf(record.amount.value)
            val convertedAmount = record.transferAmount.value
            val convertedAmountCurrency = record.transferAccount.currency.unit.code

            showAndUpdateConversionData(
                conversionRate = conversionRate,
                convertedAmount = convertedAmount,
                convertedAmountCurrency = convertedAmountCurrency
            )
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.setRecordType(type: RecordType) {
        if (type != state.selectedType) {
            val isTransferType = type == RecordType.Transfer

            reduce {
                state.copy(
                    selectedType = type,
                    showTransferDisclaimerButton = isTransferType,
                    showTransferAccountInput = isTransferType,
                    hideCategoryInput = isTransferType,
                )
            }
            if (!isTransferType) {
                clearTransferAccount()
                hideAndClearConversionData()
            }
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.setCategory(category: Category) {
        selectedCategory = category
        reduce {
            state.copy(
                categoryInput = state.categoryInput.copy(
                    value = category.name,
                    error = validateRequiredInput(category.name).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.setAccount(account: Account) {
        selectedAccount = account

        if (account.id == selectedTransferAccount?.id) {
            clearTransferAccount()
        }

        reduce {
            state.copy(
                amountCurrency = account.currency.unit.code,
                amountInput = state.amountInput.copy(
                    value = state.amountInput.value.setScaleRoundingDown(account.currency.unit.scale)
                ),
                accountInput = state.accountInput.copy(
                    value = account.displayName,
                    error = validateRequiredInput(account.displayName).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.setTransferAccount(account: Account) {
        selectedTransferAccount = account
        reduce {
            state.copy(
                transferAccountInput = state.transferAccountInput.copy(
                    value = account.displayName,
                    error = validateRequiredInput(account.displayName).errorRes
                )
            )
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.clearTransferAccount() {
        selectedTransferAccount = null
        reduce {
            state.copy(
                transferAccountInput = state.transferAccountInput.copy(
                    value = "",
                    error = null
                )
            )
        }
    }

    private suspend fun SimpleSyntax<RecordDetailsState, *>.dismissDialog() {
        reduce { state.copy(dialog = null) }
    }

    private fun getTransferAccountSelection() = LongNavArg(
        value = selectedTransferAccount?.id ?: NULL_ID,
        resultCode = transferAccountSelectionResultCode
    )

    private fun getAccountSelection() = LongNavArg(
        value = selectedAccount?.id ?: NULL_ID,
        resultCode = accountSelectionResultCode
    )
}