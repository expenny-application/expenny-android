package org.expenny.feature.accounts.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.addOrRemoveIfExist
import org.expenny.core.common.models.StringResource.Companion.fromPluralRes
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.institution.GetInstitutionSyncEnabledUseCase
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
import org.expenny.feature.accounts.list.contract.AccountsListAction
import org.expenny.feature.accounts.list.contract.AccountsListEvent
import org.expenny.feature.accounts.list.contract.AccountsListState
import org.expenny.feature.accounts.navArgs
import org.expenny.feature.accounts.list.navigation.AccountsListNavArgs
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
    private val getInstitutionSyncEnabled: GetInstitutionSyncEnabledUseCase,
    private val accountMapper: AccountMapper,
) : ExpennyViewModel<AccountsListAction>(), ContainerHost<AccountsListState, AccountsListEvent> {

    private var selectionResultCode: Int = 0
    private var excludeIds: LongArray = LongArray(0)

    override val container = container<AccountsListState, AccountsListEvent>(
        initialState = AccountsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToAccounts() }
            launch { setupInitialState() }
        }
    }

    override fun onAction(action: AccountsListAction) {
        when (action) {
            is AccountsListAction.OnAccountClick -> handleOnAccountClick(action)
            is AccountsListAction.OnConfirmSelectionClick -> handleOnConfirmSelectionClick()
            is AccountsListAction.OnAccountCreateClick -> handleOnAccountCreateClick()
            is AccountsListAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnAccountCreateClick() = intent {
        if (getInstitutionSyncEnabled()) {
            postSideEffect(AccountsListEvent.NavigateToAccountType)
        } else {
            postSideEffect(AccountsListEvent.NavigateToCreateAccount)
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(AccountsListEvent.NavigateBack)
    }

    private fun handleOnConfirmSelectionClick() = intent {
        val results = (state.selection as MultiSelectionUi<Long>).value.toLongArray()
        postSideEffect(AccountsListEvent.NavigateBackWithResult(LongArrayNavArg(results)))
    }

    private fun handleOnAccountClick(action: AccountsListAction.OnAccountClick) = intent {
        when (val selection = state.selection) {
            is SingleSelectionUi -> {
                postSideEffect(AccountsListEvent.NavigateBackWithResult(LongNavArg(action.id, selectionResultCode)))
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
                postSideEffect(AccountsListEvent.NavigateToEditAccount(action.id))
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