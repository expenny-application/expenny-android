package org.expenny.feature.budgets.limit

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.utils.Constants
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.budget.CreateBudgetUseCase
import org.expenny.core.domain.usecase.budget.DeleteBudgetUseCase
import org.expenny.core.domain.usecase.budgetgroup.GetBudgetGroupCurrencyUseCase
import org.expenny.core.domain.usecase.budget.GetBudgetUseCase
import org.expenny.core.domain.usecase.budget.GetBudgetsUseCase
import org.expenny.core.domain.usecase.budget.UpdateBudgetUseCase
import org.expenny.core.domain.usecase.category.GetCategoriesUseCase
import org.expenny.core.domain.usecase.category.GetCategoryUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.validators.BigDecimalConstraintsValidator
import org.expenny.core.domain.validators.RequiredBigDecimalValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.model.category.Category
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsAction
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsEvent
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsState
import org.expenny.feature.budgets.limit.navigation.BudgetLimitDetailsNavArgs
import org.expenny.feature.budgets.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BudgetLimitDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createBudget: CreateBudgetUseCase,
    private val updateBudget: UpdateBudgetUseCase,
    private val getBudgets: GetBudgetsUseCase,
    private val deleteBudget: DeleteBudgetUseCase,
    private val getBudgetGroupCurrency: GetBudgetGroupCurrencyUseCase,
    private val getBudget: GetBudgetUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val validateInput: ValidateInputUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getCategory: GetCategoryUseCase,
)  : ExpennyViewModel<BudgetLimitDetailsAction>(), ContainerHost<BudgetLimitDetailsState, BudgetLimitDetailsEvent>  {

    private var budgetId: Long? = null
    private var selectedCategory: Category? = null

    private var excludeCategoriesIds: LongArray =
        savedStateHandle.navArgs<BudgetLimitDetailsNavArgs>().excludeCategoriesIds
    private var budgetGroupId: Long =
        savedStateHandle.navArgs<BudgetLimitDetailsNavArgs>().budgetGroupId
    private var dateRange: ClosedRange<LocalDate> =
        savedStateHandle.navArgs<BudgetLimitDetailsNavArgs>().let { it.startDate.rangeTo(it.endDate) }

    override val container = container<BudgetLimitDetailsState, BudgetLimitDetailsEvent>(
        initialState = BudgetLimitDetailsState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { setupInitialState() }
        }
    }

    private val stateValue
        get() = container.stateFlow.value

    override fun onAction(action: BudgetLimitDetailsAction) {
        when (action) {
            is BudgetLimitDetailsAction.OnLimitChange -> handleOnLimitChange(action)
            is BudgetLimitDetailsAction.OnCategorySelect -> handleOnCategorySelect(action)
            is BudgetLimitDetailsAction.OnDeleteDialogConfirm -> handleOnDeleteDialogConfirm()
            is BudgetLimitDetailsAction.OnDeleteDialogDismiss -> handleOnDeleteDialogDismiss()
            is BudgetLimitDetailsAction.OnSelectCategoryClick -> handleOnSelectCategoryClick()
            is BudgetLimitDetailsAction.OnEnablePeriodicLimitCheckboxChange -> handleOnEnablePeriodicLimitCheckboxChange(action)
            is BudgetLimitDetailsAction.OnSaveClick -> handleOnSaveClick()
            is BudgetLimitDetailsAction.OnDeleteClick -> handleOnDeleteClick()
            is BudgetLimitDetailsAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnEnablePeriodicLimitCheckboxChange(action: BudgetLimitDetailsAction.OnEnablePeriodicLimitCheckboxChange) = intent {
        reduce {
            state.copy(enablePeriodicLimitCheckboxInput = state.enablePeriodicLimitCheckboxInput.copy(value = action.isChecked))
        }
    }

    private fun handleOnSelectCategoryClick() = intent {
        val selection = LongNavArg(selectedCategory?.id ?: Constants.NULL_ID)
        postSideEffect(BudgetLimitDetailsEvent.NavigateToCategorySelectionList(selection, excludeCategoriesIds))
    }

    private fun handleOnDeleteDialogDismiss() = intent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun handleOnDeleteDialogConfirm() = intent {
        budgetId?.let {
            deleteBudget(it, dateRange)

            reduce { state.copy(showDeleteDialog = false) }

            postSideEffect(BudgetLimitDetailsEvent.ShowMessage(StringResource.fromRes(R.string.deleted_message)))
            postSideEffect(BudgetLimitDetailsEvent.NavigateBack)
        }
    }

    private fun handleOnCategorySelect(action: BudgetLimitDetailsAction.OnCategorySelect) = intent {
        getCategory(GetCategoryUseCase.Params(action.selection.value))!!.also { category ->
            selectedCategory = category

            reduce {
                state.copy(
                    categoryInput = state.categoryInput.copy(
                        value = category.name,
                        error = validateCategory(category.name).errorRes
                    )
                )
            }
        }
    }

    private fun handleOnLimitChange(action: BudgetLimitDetailsAction.OnLimitChange) = blockingIntent {
        reduce {
            state.copy(
                limitInput = state.limitInput.copy(
                    value = action.limit,
                    error = validateLimit(action.limit).errorRes
                )
            )
        }
    }

    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            if (budgetId == null) {
                createBudget(
                    budgetGroupId = budgetGroupId,
                    categoryId = selectedCategory!!.id,
                    limitValue = stateValue.limitInput.value,
                    isOpened = stateValue.enablePeriodicLimitCheckboxInput.value,
                    dateRange = dateRange
                )
            } else {
                updateBudget(
                    id = budgetId!!,
                    budgetGroupId = budgetGroupId,
                    limitValue = stateValue.limitInput.value,
                    isOpened = stateValue.enablePeriodicLimitCheckboxInput.value,
                    dateRange = dateRange
                )
            }
            postSideEffect(BudgetLimitDetailsEvent.ShowMessage(StringResource.fromRes(R.string.saved_message)))
            postSideEffect(BudgetLimitDetailsEvent.NavigateBack)
        }
    }

    private fun handleOnDeleteClick() = intent {
        budgetId?.let {
            reduce { state.copy(showDeleteDialog = true) }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(BudgetLimitDetailsEvent.NavigateBack)
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(BudgetLimitDetailsEvent.ShowError(message.text))
        }
    }

    private fun validateFields(): Boolean {
        return with(container.stateFlow.value) {
            val limitValidationResult = validateLimit(limitInput.value)
            val categoryValidationResult = validateCategory(categoryInput.value)

            intent {
                reduce {
                    state.copy(
                        limitInput = state.limitInput.copy(error = limitValidationResult.errorRes),
                        categoryInput = state.categoryInput.copy(error = categoryValidationResult.errorRes),
                    )
                }
            }
            return@with listOf(
                limitValidationResult,
                categoryValidationResult,
            ).all { it.isValid }
        }
    }

    private fun validateCategory(value: String) = validateInput(
        value, listOf(RequiredStringValidator())
    )

    private fun validateLimit(value: BigDecimal) = validateInput(
        value.toPlainString(),
        listOf(RequiredBigDecimalValidator(), BigDecimalConstraintsValidator(min = BigDecimal.ZERO))
    )

    private fun setupInitialState() {
        savedStateHandle.navArgs<BudgetLimitDetailsNavArgs>().also { args ->
            if (args.budgetId != null) {
                setEditBudgetLimitState(args)
            } else {
                setAddBudgetLimitState(args)
            }
        }
    }

    private fun setAddBudgetLimitState(args: BudgetLimitDetailsNavArgs) = intent {
        val currency = getBudgetGroupCurrency(args.budgetGroupId).first()!!
        val suggestedCategory = getSuggestedCategory()

        reduce {
            state.copy(
                toolbarTitle = StringResource.fromRes(R.string.add_budget_limit_label),
                currency = currency.unit.code,
                categoryInput = state.categoryInput.copy(
                    value = suggestedCategory?.name ?: "",
                    isEnabled = suggestedCategory == null
                ),
                limitInput = state.limitInput.copy(
                    value = state.limitInput.value.setScale(currency.unit.scale),
                ),
                showEnablePeriodicLimitCheckbox = args.budgetType == BudgetType.Periodic,
            )
        }
        postSideEffect(BudgetLimitDetailsEvent.RequestLimitInputFocus)
    }

    private fun setEditBudgetLimitState(args: BudgetLimitDetailsNavArgs) = intent {
        val currency = getBudgetGroupCurrency(args.budgetGroupId).first()!!
        val budget = getBudget(args.budgetId!!).first()!!

        budgetId = budget.id
        selectedCategory = budget.category

        reduce {
            state.copy(
                toolbarTitle = StringResource.fromRes(R.string.edit_budget_limit_label),
                showDeleteButton = true,
                currency = currency.unit.code,
                categoryInput = state.categoryInput.copy(
                    value = budget.category.name,
                    isEnabled = false
                ),
                limitInput = state.limitInput.copy(
                    value = budget.limitValue.setScale(currency.unit.scale)
                ),
                showEnablePeriodicLimitCheckbox = args.budgetType == BudgetType.Periodic,
                enablePeriodicLimitCheckboxInput = state.enablePeriodicLimitCheckboxInput.copy(
                    value = budget.endDate == null
                )
            )
        }
        postSideEffect(BudgetLimitDetailsEvent.RequestLimitInputFocus)
    }

    private suspend fun getSuggestedCategory(): Category? {
        return getCategories().map { categories ->
            categories.filter { it.id !in excludeCategoriesIds }
        }.first().let {
            if (it.size == 1) it.first() else null
        }
    }
}