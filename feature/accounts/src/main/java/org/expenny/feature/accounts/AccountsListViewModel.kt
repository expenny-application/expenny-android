package org.expenny.feature.accounts

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.addOrRemoveIfExist
import org.expenny.core.common.models.StringResource.Companion.fromPluralRes
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.domain.usecase.account.GetAccountsUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.model.account.AccountRecords
import org.expenny.core.model.record.Record
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.MultiSelectionUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.mapper.AccountMapper
import org.expenny.feature.accounts.model.Action
import org.expenny.feature.accounts.model.Event
import org.expenny.feature.accounts.model.State
import org.expenny.feature.accounts.navigation.AccountsListNavArgs
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
    private val getAccounts: GetAccountsUseCase,
    private val getRecords: GetRecordsUseCase,
    private val accountMapper: AccountMapper
) : ExpennyViewModel<Action>(), ContainerHost<State, Event> {

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
        val results = (state.selection as MultiSelectionUi<Long>).value.toLongArray()
        postSideEffect(Event.NavigateBackWithResult(LongArrayNavArg(results)))
    }

    private fun handleOnAccountClick(action: Action.OnAccountClick) = intent {
        when (val selection = state.selection) {
            is SingleSelectionUi -> {
                postSideEffect(Event.NavigateBackWithResult(LongNavArg(action.id, selectionResultCode)))
            }
            is MultiSelectionUi -> {
                val newSelectionData = selection.value.addOrRemoveIfExist(action.id)
                reduce {
                    state.copy(
                        showConfirmButton = newSelectionData.isNotEmpty(),
                        selection = MultiSelectionUi(value = newSelectionData)
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
            combine(getAccounts(), getRecords()) { accounts, records ->
                accounts
                    .filter { it.id !in excludeIds }
                    .map { account ->
                        val accountRecords = records.filter {
                            if (it is Record.Transfer) {
                                it.transferAccount.id == account.id || it.account.id == account.id
                            } else {
                                it.account.id == account.id
                            }
                        }
                        AccountRecords(account, accountRecords)
                    }
            }.collect {
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
                            is LongArrayNavArg -> MultiSelectionUi(args.selection.values.toList())
                            is LongNavArg -> SingleSelectionUi(args.selection.value)
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