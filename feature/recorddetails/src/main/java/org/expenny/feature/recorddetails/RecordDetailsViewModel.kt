package org.expenny.feature.recorddetails

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.setScaleNoRounding
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.common.extensions.toLocalDate
import org.expenny.core.common.extensions.toLocalTime
import org.expenny.core.common.extensions.toTimeString
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.utils.Constants.NULL_ID
import org.expenny.core.common.utils.ErrorMessage
import org.expenny.core.common.utils.ExternalFilesDirectoryHandler
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.account.GetAccountUseCase
import org.expenny.core.domain.usecase.account.GetLastUsedAccountUseCase
import org.expenny.core.domain.usecase.category.GetCategoryUseCase
import org.expenny.core.domain.usecase.category.GetMostUsedCategoryUseCase
import org.expenny.core.domain.usecase.record.CreateRecordUseCase
import org.expenny.core.domain.usecase.record.DeleteRecordUseCase
import org.expenny.core.domain.usecase.record.GetRecordLabelsUseCase
import org.expenny.core.domain.usecase.record.GetRecordUseCase
import org.expenny.core.domain.usecase.record.UpdateRecordUseCase
import org.expenny.core.domain.validators.AlphanumericValidator
import org.expenny.core.domain.validators.RequiredBigDecimalValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.model.account.Account
import org.expenny.core.model.category.Category
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.record.Record
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.mapper.AccountNameMapper
import org.expenny.feature.recorddetails.navigation.RecordDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
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
    private val getLabels: GetRecordLabelsUseCase,
    private val getRecord: GetRecordUseCase,
    private val getAccount: GetAccountUseCase,
    private val getCategory: GetCategoryUseCase,
    private val getLastCreatedAccount: GetLastUsedAccountUseCase,
    private val getLastUsedCategory: GetMostUsedCategoryUseCase,
    private val accountNameMapper: AccountNameMapper,
    private val filesDirectoryHandler: ExternalFilesDirectoryHandler,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private var maxReceipts = 3
    private val accountSelectionResultCode = 1
    private val transferAccountSelectionResultCode = 2
    private var isTransferResetConfirmed: Boolean = false
    private var transferResetTargetType: RecordType? = null
    private val currentRecord = MutableStateFlow<Record?>(null)
    private val selectedCategory = MutableStateFlow<Category?>(null)
    private val selectedAccount = MutableStateFlow<Account?>(null)
    private val selectedTransferAccount = MutableStateFlow<Account?>(null)
    private var labelsAcrossAllRecords = emptyList<String>()

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setInitialData()
            launch { subscribeToSelectedCategory() }
            launch { subscribeToSelectedAccount() }
            launch { subscribeToSelectedTransferAccount() }
            launch { getLabelsAcrossAllRecords() }
        }
    }

    private val state get() = container.stateFlow.value

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnTypeChange -> handleOnTypeChange(action)
            is Action.OnAmountChange -> handleOnAmountChange(action)
            is Action.OnTransferAmountChange -> handleOnTransferAmountChange(action)
            is Action.OnDescriptionChange -> handleOnDescriptionChange(action)
            is Action.OnLabelChange -> handleOnLabelChange(action)
            is Action.OnAdditionsSectionVisibilityChange -> handleOnAdditionsSectionVisibilityChange(action)
            is Action.OnCategorySelect -> handleOnCategorySelect(action)
            is Action.OnAccountSelect -> handleOnAccountSelect(action)
            is Action.OnLabelAdd -> handleOnLabelAdd(action)
            is Action.OnLabelRemove -> handleOnLabelRemove(action)
            is Action.OnSelectCategoryClick -> handleOnSelectCategoryClick()
            is Action.OnSelectAccountClick -> handleOnSelectAccountClick()
            is Action.OnSelectTransferAccountClick -> handleOnSelectTransferAccountClick()
            is Action.OnSelectDateClick -> handleOnSelectDateClick()
            is Action.OnSelectTimeClick -> handleOnSelectTimeClick()
            is Action.OnAddReceiptClick -> handleOnAddReceiptClick()
            is Action.OnReceiptSelect -> handleOnReceiptSelect(action)
            is Action.OnReceiptCapture -> handleOnReceiptCapture(action)
            is Action.OnDeleteReceiptClick -> handleOnDeleteReceiptClick(action)
            is Action.OnViewReceiptClick -> handleOnViewReceiptClick(action)
            is Action.OnTransferDisclaimerClick -> handleOnTransferDisclaimerClick()
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnDeleteClick -> handleOnDeleteClick()
            is Action.Dialog.OnDateChange -> handleOnDateChange(action)
            is Action.Dialog.OnTimeChange -> handleOnTimeChange(action)
            is Action.Dialog.OnReceiptSourceDialogCameraSelect -> handleOnReceiptSourceDialogCameraSelect()
            is Action.Dialog.OnReceiptSourceDialogGallerySelect -> handleOnReceiptSourceDialogGallerySelect()
            is Action.Dialog.OnDeleteRecordDialogConfirm -> handleOnDeleteRecordDialogConfirm()
            is Action.Dialog.OnResetTransferDialogConfirm -> handleOnResetTransferDialogConfirm()
            is Action.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
        }
    }

    private fun getTransferAmount(): BigDecimal {
        return CurrencyAmount(
            currency = selectedAccount.value!!.currency,
            amountValue = state.amountInput.value
        ).convertTo(selectedTransferAccount.value!!.currency).value
    }

    private fun getSelectedDate(): LocalDateTime? {
        val date = state.dateInput.value.toLocalDate()
        val time = state.timeInput.value.toLocalTime()
        return date?.atTime(time)
    }

    private fun getTransferAccountSelection() = LongNavArg(
        value = selectedTransferAccount.value?.id ?: NULL_ID,
        resultCode = transferAccountSelectionResultCode
    )

    private fun getAccountSelection() = LongNavArg(
        value = selectedAccount.value?.id ?: NULL_ID,
        resultCode = accountSelectionResultCode
    )

    private fun handleOnLabelChange(action: Action.OnLabelChange) = blockingIntent {
        val suggestedLabel = labelsAcrossAllRecords
            .sortedBy { it.length }
            .firstOrNull { it.startsWith(action.label) }

        reduce {
            state.copy(
                labelsInput = state.labelsInput.copy(
                    value = action.label,
                    suggestion = suggestedLabel,
                    error = validateAlphanumericInput(action.label).errorRes
                )
            )
        }
    }

    private fun handleOnLabelAdd(action: Action.OnLabelAdd) = blockingIntent {
        reduce {
            state.copy(
                labelsInput = state.labelsInput.copy(
                    labels = state.labelsInput.labels + action.label
                )
            )
        }
    }

    private fun handleOnLabelRemove(action: Action.OnLabelRemove) = intent {
        reduce {
            state.copy(
                labelsInput = state.labelsInput.copy(
                    labels = state.labelsInput.labels.filterIndexed { i, _ ->
                        action.index != i
                    }
                )
            )
        }
    }

    private fun handleOnReceiptSourceDialogCameraSelect() = intent {
        dismissDialog()
        val uri = filesDirectoryHandler.getPersistentImageUri()
        postSideEffect(Event.OpenCamera(uri))
    }

    private fun handleOnReceiptSourceDialogGallerySelect() = intent {
        dismissDialog()
        postSideEffect(Event.OpenImagePicker)
    }

    private fun handleOnCategorySelect(action: Action.OnCategorySelect) = intent {
        selectedCategory.value = getCategory(GetCategoryUseCase.Params(action.selection.value))!!
    }

    private fun handleOnAccountSelect(action: Action.OnAccountSelect) = intent {
        val account = getAccount(GetAccountUseCase.Params(action.selection.value))!!

        when (action.selection.resultCode) {
            accountSelectionResultCode -> selectedAccount.value = account
            transferAccountSelectionResultCode -> selectedTransferAccount.value = account
        }

        if (state.selectedType == RecordType.Transfer) {
            val currency = selectedAccount.value?.currency
            val transferCurrency = selectedTransferAccount.value?.currency

            if (selectedAccount.value?.id == selectedTransferAccount.value?.id) {
                resetSelectedTransferAccount()
            }

            if (currency?.id != null && transferCurrency?.id != null && currency.id != transferCurrency.id) {
                reduce {
                    state.copy(
                        dialog = State.Dialog.ConversionDialog,
                        showTransferAmountInput = true,
                        transferAmountCurrency = transferCurrency.unit.code,
                        transferAmountInput = state.transferAmountInput.copy(
                            value = getTransferAmount()
                        )
                    )
                }
            } else {
                hideAndResetTransferAmountInput()
            }
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(Event.ShowMessage(message.text))
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnAmountChange(action: Action.OnAmountChange) = blockingIntent {
        reduce {
            state.copy(
                amountInput = state.amountInput.copy(
                    value = action.amount,
                    error = validateRequiredInput(action.amount).errorRes
                )
            )
        }
    }

    private fun handleOnTransferAmountChange(action: Action.OnTransferAmountChange) = blockingIntent {
        reduce {
            state.copy(
                transferAmountInput = state.transferAmountInput.copy(
                    value = action.amount,
                    error = validateRequiredInput(action.amount).errorRes
                )
            )
        }
    }

    private fun handleOnSelectCategoryClick() = intent {
        val selection = LongNavArg(selectedCategory.value?.id ?: NULL_ID)
        postSideEffect(Event.NavigateToCategorySelectionList(selection))
    }

    private fun handleOnSelectAccountClick() = intent {
        val selection = getAccountSelection()
        postSideEffect(Event.NavigateToAccountSelectionList(selection))
    }

    private fun handleOnSelectTransferAccountClick() = intent {
        val selection = getTransferAccountSelection()
        val excludeIds = selectedAccount.value?.id?.let { longArrayOf(it) }
        postSideEffect(Event.NavigateToAccountSelectionList(selection, excludeIds))
    }

    private fun handleOnDateChange(action: Action.Dialog.OnDateChange) = intent {
        reduce {
            state.copy(dateInput = state.dateInput.copy(value = action.date.toDateString()))
        }
    }

    private fun handleOnTimeChange(action: Action.Dialog.OnTimeChange) = intent {
        reduce {
            state.copy(timeInput = state.timeInput.copy(value = action.time.toTimeString()))
        }
    }

    private fun handleOnDescriptionChange(action: Action.OnDescriptionChange) = blockingIntent {
        reduce {
            state.copy(descriptionInput = state.descriptionInput.copy(value = action.description))
        }
    }

    private fun handleOnAdditionsSectionVisibilityChange(action: Action.OnAdditionsSectionVisibilityChange) = intent {
        reduce {
            state.copy(showAdditionsSection = action.isVisible)
        }
    }

    private fun handleOnAddReceiptClick() = intent {
        if (state.receipts.size < maxReceipts) {
            reduce {
                state.copy(dialog = State.Dialog.ReceiptSourceDialog)
            }
        } else {
            postSideEffect(Event.ShowMessage(fromRes(R.string.max_photos_amount_error, maxReceipts)))
        }
    }

    private fun handleOnReceiptSelect(action: Action.OnReceiptSelect) = intent {
        if (action.uri != null) {
            val uri = filesDirectoryHandler.copyFileToPersistentDirectory(action.uri)
            reduce {
                state.copy(receipts = state.receipts + uri!!)
            }
        }
    }

    private fun handleOnReceiptCapture(action: Action.OnReceiptCapture) = intent {
        if (action.uri != null) {
            reduce {
                state.copy(receipts = state.receipts + action.uri)
            }
        }
    }

    private fun handleOnDeleteReceiptClick(action: Action.OnDeleteReceiptClick) = intent {
        reduce {
            state.copy(receipts = state.receipts - action.uri)
        }
    }

    private fun handleOnTransferDisclaimerClick() = intent {
        reduce { state.copy(dialog = State.Dialog.TransferDisclaimerDialog) }
    }

    private fun handleOnViewReceiptClick(action: Action.OnViewReceiptClick) = intent {
        postSideEffect(Event.OpenImageViewer(action.uri))
    }

    private fun handleOnTypeChange(action: Action.OnTypeChange) = intent {
        if (action.type != state.selectedType) {
            if (state.selectedType == RecordType.Transfer && !isTransferResetConfirmed && selectedTransferAccount.value != null) {
                transferResetTargetType = action.type
                reduce {
                    state.copy(dialog = State.Dialog.ResetTransferDialog)
                }
            } else {
                val isNewTypeTransfer = action.type == RecordType.Transfer
                reduce {
                    state.copy(
                        selectedType = action.type,
                        showTransferAmountInput = false,
                        showTransferDisclaimerButton = isNewTypeTransfer,
                        showTransferAccountInput = isNewTypeTransfer,
                        showCategoryInput = !isNewTypeTransfer,
                    )
                }

                resetTransferResetTargetType()
                resetTransferResetConfirmation()
                resetSelectedTransferAccount()
                hideAndResetTransferAmountInput()
            }
        }
    }

    private fun hideAndResetTransferAmountInput() = intent {
        reduce {
            state.copy(
                showTransferAmountInput = false,
                transferAmountInput = state.transferAmountInput.copy(
                    value = BigDecimal.ZERO,
                    error = null
                )
            )
        }
    }

    private fun handleOnSaveClick() {
        if (validateFields()) {
            intent {
                val transferAmount = when (state.selectedType) {
                    RecordType.Transfer -> getTransferAmount()
                    else -> null
                }

                if (currentRecord.value == null) {
                    createRecord(
                        CreateRecordUseCase.Params(
                            type = state.selectedType,
                            accountId = selectedAccount.value!!.id,
                            transferAccountId = selectedTransferAccount.value?.id,
                            categoryId = selectedCategory.value?.id,
                            receiptsUris = state.receipts,
                            description = state.descriptionInput.value,
                            labels = state.labelsInput.labels,
                            amount = state.amountInput.value,
                            transferAmount = transferAmount,
                            transferFee = BigDecimal.ZERO,
                            date = getSelectedDate() ?: LocalDateTime.now()
                        )
                    )
                } else {
                    updateRecord(
                        UpdateRecordUseCase.Params(
                            id = currentRecord.value!!.id,
                            type = state.selectedType,
                            accountId = selectedAccount.value!!.id,
                            transferAccountId = selectedTransferAccount.value?.id,
                            categoryId = selectedCategory.value?.id,
                            receiptsUris = state.receipts,
                            description = state.descriptionInput.value,
                            labels = state.labelsInput.labels,
                            amount = state.amountInput.value,
                            transferAmount = transferAmount,
                            transferFee = BigDecimal.ZERO,
                            date = getSelectedDate() ?: LocalDateTime.now()
                        )
                    )
                }
                postSideEffect(Event.ShowMessage(fromRes(R.string.saved_message)))
                postSideEffect(Event.NavigateBack)
            }
        }
    }

    private fun handleOnSelectDateClick() = intent {
        reduce { state.copy(dialog = State.Dialog.DatePickerDialog) }
    }

    private fun handleOnSelectTimeClick() = intent {
        reduce { state.copy(dialog = State.Dialog.TimePickerDialog) }
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(dialog = State.Dialog.DeleteRecordDialog) }
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        dismissDialog()
        deleteRecord(currentRecord.value!!.id)
        postSideEffect(Event.ShowMessage(fromRes(R.string.deleted_message)))
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnResetTransferDialogConfirm() = intent {
        dismissDialog()
        isTransferResetConfirmed = true
        handleOnTypeChange(Action.OnTypeChange(transferResetTargetType!!))
    }

    private fun handleOnDialogDismiss() = intent {
        dismissDialog()
    }

    private fun Record.Transfer.showTransferAmountInput(): Boolean {
        return account != transferAccount && account.currency.id != transferAccount.currency.id
    }

    private fun resetTransferResetTargetType() {
        transferResetTargetType = null
    }

    private fun resetTransferResetConfirmation() {
        isTransferResetConfirmed = false
    }

    private fun resetSelectedTransferAccount() {
        selectedTransferAccount.value = null
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

    private fun validateFields(): Boolean {
        val amountValidationResult = validateRequiredInput(state.amountInput.value)
        val accountValidationResult = validateRequiredInput(state.accountInput.value)

        intent {
            reduce {
                state.copy(
                    amountInput = state.amountInput.copy(error = amountValidationResult.errorRes),
                    accountInput = state.accountInput.copy(error = accountValidationResult.errorRes),
                )
            }
        }

        val fieldsValidationResults = mutableListOf(amountValidationResult, accountValidationResult)

        if (state.selectedType == RecordType.Transfer) {
            intent {
                val transferAccountValidationResult = validateRequiredInput(state.transferAccountInput.value)
                fieldsValidationResults.add(transferAccountValidationResult)

                reduce {
                    state.copy(
                        transferAccountInput = state.transferAccountInput.copy(error = transferAccountValidationResult.errorRes)
                    )
                }

                if (state.showTransferAmountInput) {
                    val transferAmountValidationResult = validateRequiredInput(state.transferAmountInput.value)
                    fieldsValidationResults.add(transferAmountValidationResult)

                    reduce {
                        state.copy(
                            transferAmountInput = state.transferAmountInput.copy(error = transferAmountValidationResult.errorRes)
                        )
                    }
                }
            }
        } else {
            val categoryValidationResult = validateRequiredInput(state.categoryInput.value)
            fieldsValidationResults.add(categoryValidationResult)

            intent {
                reduce {
                    state.copy(
                        categoryInput = state.categoryInput.copy(error = categoryValidationResult.errorRes)
                    )
                }
            }
        }

        return fieldsValidationResults.all { it.isValid }
    }

    private suspend fun getLabelsAcrossAllRecords() {
        labelsAcrossAllRecords = getLabels().first()
    }

    private fun subscribeToSelectedTransferAccount() = intent {
        repeatOnSubscription {
            selectedTransferAccount.collect {
                val value = it?.let { accountNameMapper(it).displayName }.orEmpty()
                val error = if (value.isNotBlank()) validateRequiredInput(value).errorRes else null

                reduce {
                    state.copy(transferAccountInput = state.transferAccountInput.copy(value, error))
                }
            }
        }
    }

    private fun subscribeToSelectedCategory() = intent {
        repeatOnSubscription {
            selectedCategory.collect {
                val value = it?.name.orEmpty()
                val error = if (value.isNotBlank()) validateRequiredInput(value).errorRes else null

                reduce {
                    state.copy(categoryInput = state.categoryInput.copy(value, error))
                }
            }
        }
    }

    private fun subscribeToSelectedAccount() = intent {
        repeatOnSubscription {
            selectedAccount.filterNotNull().collect {
                reduce {
                    // TODO move to one place
                    state.copy(
                        amountCurrency = it.currency.unit.code,
                        accountInput = state.accountInput.copy(
                            value = accountNameMapper(it).displayName
                        ),
                        amountInput = state.amountInput.copy(
                            value = state.amountInput.value.setScaleNoRounding(it.currency.unit.scale),
                            isEnabled = true,
                        )
                    )
                }
            }
        }
    }

    private fun setInitialData() {
        savedStateHandle.navArgs<RecordDetailsNavArgs>().also { args ->
            intent {
                if (args.recordId != null) {
                    val record = getRecord(GetRecordUseCase.Params(args.recordId)).first()

                    if (record != null) {
                        setInitialRecordData(record, args.isClone)

                        when (record) {
                            is Record.Transfer -> setInitialTransferData(record)
                            is Record.Transaction -> setInitialTransactionData(record)
                        }
                    } else {
                        postSideEffect(Event.ShowMessage(fromRes(R.string.invalid_argument_error)))
                        postSideEffect(Event.NavigateBack)
                    }
                } else {
                    val recordType = args.recordType ?: RecordType.Expense

                    selectedAccount.value = getLastCreatedAccount()

                    if (recordType != RecordType.Transfer) {
                        selectedCategory.value = getLastUsedCategory()
                        reduce {
                            state.copy(showCategoryInput = true)
                        }
                    }

                    handleOnTypeChange(Action.OnTypeChange(recordType))

                    postSideEffect(Event.RequestAmountInputFocus)
                }
            }
        }
    }

    private fun setInitialRecordData(record: Record, isClone: Boolean) = intent {
        selectedAccount.value = record.account

        if (!isClone) {
            currentRecord.value = record
        }

        reduce {
            state.copy(
                showDeleteButton = !isClone,
                toolbarTitle = when {
                    isClone -> fromRes(R.string.add_record_label)
                    else -> fromRes(R.string.edit_record_label)
                },
                selectedType = record.recordType,
                receipts = record.receipts,
                labelsInput = state.labelsInput.copy(labels = record.labels),
                amountInput = state.amountInput.copy(value = record.amount.value),
                dateInput = state.dateInput.copy(value = record.date.toDateString()),
                timeInput = state.timeInput.copy(value = record.date.toTimeString()),
                descriptionInput = state.descriptionInput.copy(value = record.description),
            )
        }
    }

    private fun setInitialTransactionData(record: Record.Transaction) = intent {
        selectedCategory.value = record.category

        reduce {
            state.copy(showCategoryInput = true)
        }
    }

    private fun setInitialTransferData(record: Record.Transfer) = intent {
        selectedTransferAccount.value = record.transferAccount

        val showTransferAmountInput = record.showTransferAmountInput()

        reduce {
            state.copy(
                showTransferDisclaimerButton = true,
                showTransferAccountInput = true
            )
        }

        if (showTransferAmountInput) {
            reduce {
                state.copy(
                    showTransferAmountInput = true,
                    transferAmountCurrency = record.transferAccount.currency.unit.code,
                    transferAmountInput = state.transferAmountInput.copy(
                        value = record.transferAmount.value
                    )
                )
            }
        }
    }

    private suspend fun SimpleSyntax<State, Event>.dismissDialog() {
        reduce { state.copy(dialog = null) }
    }
}