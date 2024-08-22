package org.expenny.feature.budgets.list

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.ItemActionType
import org.expenny.core.domain.usecase.budgetgroup.CreatePeriodicBudgetGroupUseCase
import org.expenny.core.domain.usecase.budgetgroup.DeleteBudgetGroupUseCase
import org.expenny.core.domain.usecase.budgetgroup.GetPeriodicBudgetGroupsUseCase
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.mapper.PeriodicBudgetMapper
import org.expenny.core.resources.R
import org.expenny.feature.budgets.list.contract.BudgetsListAction
import org.expenny.feature.budgets.list.contract.BudgetsListEvent
import org.expenny.feature.budgets.list.contract.BudgetsListState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BudgetsListViewModel @Inject constructor(
    private val getPeriodicBudgets: GetPeriodicBudgetGroupsUseCase,
    private val createPeriodicBudgetGroup: CreatePeriodicBudgetGroupUseCase,
    private val deleteBudgetGroup: DeleteBudgetGroupUseCase,
    private val periodicBudgetMapper: PeriodicBudgetMapper
) : ExpennyViewModel<BudgetsListAction>(), ContainerHost<BudgetsListState, BudgetsListEvent> {

    override val container = container<BudgetsListState, BudgetsListEvent>(
        initialState = BudgetsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToPeriodicBudgets() }
        }
    }

    override fun onAction(action: BudgetsListAction) {
        when (action) {
            is BudgetsListAction.OnPeriodicBudgetClick -> handleOnPeriodicBudgetClick(action)
            is BudgetsListAction.OnOnetimeBudgetCreateClick -> handleOnOnetimeBudgetCreateClick()
            is BudgetsListAction.OnPeriodicBudgetCreateClick -> handleOnPeriodicBudgetCreateClick(action)
            is BudgetsListAction.OnPeriodicBudgetDeleteClick -> {}
            is BudgetsListAction.OnBudgetTypeChange -> handleOnBudgetTypeChange(action)
            is BudgetsListAction.OnBackClick -> handleOnBackClick()
            is BudgetsListAction.OnBudgetActionSelect -> handleOnBudgetActionSelect(action)
            is BudgetsListAction.OnDialogDismiss -> handleOnDialogDismiss()
            is BudgetsListAction.OnPeriodicBudgetLongClick -> handleOnPeriodicBudgetLongClick(action)
            is BudgetsListAction.OnDeleteBudgetDialogConfirm -> handleOnDeleteBudgetDialogConfirm()
        }
    }

    private fun handleOnDeleteBudgetDialogConfirm() = intent {
        val budgetGroupId = (state.dialog as? BudgetsListState.Dialog.DeleteBudgetDialog)?.id

        if (budgetGroupId != null) {
            deleteBudgetGroup(budgetGroupId)
            reduce {
                state.copy(dialog = null)
            }
            postSideEffect(BudgetsListEvent.ShowMessage(StringResource.fromRes(R.string.deleted_message)))
        }
    }

    private fun handleOnDialogDismiss() = intent {
        reduce {
            state.copy(dialog = null)
        }
    }

    private fun handleOnPeriodicBudgetLongClick(action: BudgetsListAction.OnPeriodicBudgetLongClick) = intent {
        val dialog = BudgetsListState.Dialog.BudgetActionsDialog(action.id, BudgetType.Periodic, action.intervalType)
        reduce {
            state.copy(dialog = dialog)
        }
    }

    private fun handleOnBudgetActionSelect(action: BudgetsListAction.OnBudgetActionSelect) = intent {
        val budgetGroupId = (state.dialog as BudgetsListState.Dialog.BudgetActionsDialog).id
        val budgetType = (state.dialog as BudgetsListState.Dialog.BudgetActionsDialog).type
        val intervalType = (state.dialog as BudgetsListState.Dialog.BudgetActionsDialog).intervalType

        reduce {
            state.copy(dialog = null)
        }

        when (action.action) {
            ItemActionType.View -> {
                when (budgetType) {
                    BudgetType.Periodic -> {
                        postSideEffect(BudgetsListEvent.NavigateToBudgetOverview(budgetGroupId, intervalType!!))
                    }
                    BudgetType.Onetime -> {
                        // TODO
                    }
                }
            }
            ItemActionType.Delete -> {
                reduce {
                    state.copy(dialog = BudgetsListState.Dialog.DeleteBudgetDialog(budgetGroupId))
                }
            }
            ItemActionType.Share -> {
                // TODO
            }
            else -> {}
        }
    }

    private fun handleOnBudgetTypeChange(action: BudgetsListAction.OnBudgetTypeChange) = intent {
        if (state.selectedBudgetType != action.type) {
            reduce {
                state.copy(selectedBudgetType = action.type)
            }
        }
    }

    private fun handleOnOnetimeBudgetCreateClick() = intent {
        postSideEffect(BudgetsListEvent.NavigateToOnetimeBudgetCreate)
    }

    private fun handleOnPeriodicBudgetCreateClick(action: BudgetsListAction.OnPeriodicBudgetCreateClick) = intent {
        val id = createPeriodicBudgetGroup(CreatePeriodicBudgetGroupUseCase.Params(action.type))
        postSideEffect(BudgetsListEvent.NavigateToBudgetOverview(id, action.type))
    }

    private fun handleOnPeriodicBudgetClick(action: BudgetsListAction.OnPeriodicBudgetClick) = intent {
        postSideEffect(BudgetsListEvent.NavigateToBudgetOverview(action.id, action.type))
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(BudgetsListEvent.NavigateBack)
    }

    private fun subscribeToPeriodicBudgets() = intent {
        repeatOnSubscription {
            getPeriodicBudgets().collect {
                reduce {
                    state.copy(periodicBudgets = periodicBudgetMapper(it))
                }
            }
        }
    }

    override fun onCoroutineException(message: ErrorMessage) {
        intent {
            postSideEffect(BudgetsListEvent.ShowError(message.text))
        }
    }
}