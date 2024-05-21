package org.expenny.feature.accounts

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.accounts.contract.AccountsListEvent
import org.expenny.feature.accounts.navigation.AccountsListNavArgs
import org.expenny.feature.accounts.navigation.AccountsListNavigator
import org.expenny.feature.accounts.view.AccountsListContent
import org.expenny.feature.accounts.viewmodel.AccountsListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = AccountsListNavArgs::class)
@Composable
fun AccountsListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: AccountsListNavigator,
    resultNavigator: ResultBackNavigator<NavArgResult>,
    drawerState: ExpennyDrawerManager
) {
    val vm: AccountsListViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val lazyListState = rememberLazyListState()

    vm.collectSideEffect {
        when (it) {
            is AccountsListEvent.NavigateToAccountType -> navigator.navigateToAccountTypeScreen()
            is AccountsListEvent.NavigateToCreateAccount -> navigator.navigateToCreateAccountScreen()
            is AccountsListEvent.NavigateToEditAccount -> navigator.navigateToEditAccountScreen(it.id)
            is AccountsListEvent.NavigateBackWithResult -> resultNavigator.navigateBack(it.result)
            is AccountsListEvent.NavigateBack -> navigator.navigateBack()
        }
    }

    AccountsListContent(
        state = state,
        lazyListState = lazyListState,
        drawerState = drawerState,
        onAction = vm::onAction
    )
}
