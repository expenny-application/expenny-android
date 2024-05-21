package org.expenny.feature.accounts.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.accounts.details.contract.AccountDetailsAction
import org.expenny.feature.accounts.details.contract.AccountDetailsEvent
import org.expenny.feature.accounts.details.navigation.AccountDetailsNavArgs
import org.expenny.feature.accounts.details.navigation.AccountDetailsNavigator
import org.expenny.feature.accounts.details.view.AccountDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = AccountDetailsNavArgs::class)
@Composable
fun AccountDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: AccountDetailsNavigator,
    currencyResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: AccountDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val typesListState = rememberLazyListState()
    val nameInputFocusRequester = remember { FocusRequester() }

    currencyResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(AccountDetailsAction.OnCurrencySelect(res.value.value))
        }
    }

    BackHandler(!state.showDeleteDialog) {
        vm.onAction(AccountDetailsAction.OnBackClick)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { state.selectedType }.collect {
            typesListState.animateScrollToItem(state.types.indexOf(it))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is AccountDetailsEvent.NavigateToCurrenciesSelectionList -> navigator.navigateToCurrencySelectionListScreen(it.selectedId)
            is AccountDetailsEvent.ShowMessage -> snackbarManager.showInfo(it.message)
            is AccountDetailsEvent.NavigateBack -> navigator.navigateBack()
            is AccountDetailsEvent.NavigateBackToAccountsList -> navigator.navigateBackToAccountsListScreen()
            is AccountDetailsEvent.RequestNameInputFocus -> nameInputFocusRequester.requestFocus()
        }
    }

    AccountDetailsContent(
        state = state,
        scrollState = scrollState,
        typesListState = typesListState,
        nameInputFocusRequester = nameInputFocusRequester,
        onAction = vm::onAction
    )
}