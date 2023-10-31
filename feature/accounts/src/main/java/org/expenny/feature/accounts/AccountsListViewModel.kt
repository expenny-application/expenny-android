package org.expenny.feature.accounts

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toggleItem
import org.expenny.core.common.utils.StringResource.Companion.fromPluralRes
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.account.GetAccountsWithRecordsUseCase
import org.expenny.feature.accounts.navigation.AccountsListNavArgs
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.*
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.mapper.AccountMapper
import org.expenny.feature.accounts.model.Action
import org.expenny.feature.accounts.model.Event
import org.expenny.feature.accounts.model.State
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountsListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAccountsWithRecords: GetAccountsWithRecordsUseCase,
    private val accountMapper: AccountMapper
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private var selectionResultCode: Int = 0
    private var excludeIds: LongArray = LongArray(0)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToAccounts() }
            launch { setupInitialState() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnAccountClick -> handleOnAccountClick(action)
            is Action.OnConfirmSelectionClick -> handleOnConfirmSelectionClick()
            is Action.OnAccountAddClick -> handleOnAccountCreateClick()
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnAccountCreateClick() = intent {
        postSideEffect(Event.NavigateToCreateAccount)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnConfirmSelectionClick() = intent {
        val results = (state.selection as MultiSelection<Long>).data.toLongArray()
        postSideEffect(Event.NavigateBackWithResult(LongArrayNavArg(results)))
    }

    private fun handleOnAccountClick(action: Action.OnAccountClick) = intent {
        when (val selection = state.selection) {
            is SingleSelection -> {
                postSideEffect(Event.NavigateBackWithResult(LongNavArg(action.id, selectionResultCode)))
            }
            is MultiSelection -> {
                val newSelectionData = selection.data.toggleItem(action.id)

                reduce {
                    state.copy(
                        showConfirmButton = newSelectionData.isNotEmpty(),
                        selection = MultiSelection(data = newSelectionData)
                    )
                }
            }
            else -> {
                postSideEffect(Event.NavigateToEditAccount(action.id))
            }
        }
    }

    private fun subscribeToAccounts() = intent {
        repeatOnSubscription {
            getAccountsWithRecords().collect {
                reduce {
                    state.copy(accounts = accountMapper(it).filter { it.id !in excludeIds })
                }
            }
        }
    }

    private fun setupInitialState() = intent {
        savedStateHandle.navArgs<AccountsListNavArgs>().also { args ->
            excludeIds = args.excludeIds ?: longArrayOf()

            if (args.selection != null) {
                selectionResultCode = args.selection.resultCode

                reduce {
                    state.copy(
                        toolbarTitle = when (args.selection) {
                            is LongArrayNavArg -> fromPluralRes(R.plurals.select_account_quantity_label, 2)
                            is LongNavArg -> fromPluralRes(R.plurals.select_account_quantity_label, 1)
                        },
                        selection = when (args.selection) {
                            is LongArrayNavArg -> MultiSelection(args.selection.values.toList())
                            is LongNavArg -> SingleSelection(args.selection.value)
                        }
                    )
                }
            } else {
                reduce {
                    state.copy(
                        toolbarTitle = fromRes(R.string.accounts_label),
                        selection = null
                    )
                }
            }
        }
    }
}