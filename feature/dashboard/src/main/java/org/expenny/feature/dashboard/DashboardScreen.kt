package org.expenny.feature.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.feature.dashboard.contract.DashboardAction
import org.expenny.feature.dashboard.contract.DashboardEvent
import org.expenny.feature.dashboard.navigation.DashboardNavigator
import org.expenny.feature.dashboard.style.DashboardScreenTransitionStyle
import org.expenny.feature.dashboard.view.*
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = DashboardScreenTransitionStyle::class)
@Composable
fun DashboardScreen(
    navigator: DashboardNavigator,
    currencyResult: OpenResultRecipient<LongNavArg>,
    drawerState: ExpennyDrawerManager
) {
    val vm: DashboardViewModel = hiltViewModel()
    val state by vm.collectAsState()

    val scope = rememberCoroutineScope()
    val addRecordSheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()
    val widgetsListState = rememberLazyListState()
    val accountsListState = rememberLazyListState()

    currencyResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(DashboardAction.OnDisplayCurrencySelect(res.value.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is DashboardEvent.NavigateToDisplayCurrencySelection -> {
                navigator.navigateToCurrencySelectionListScreen(it.selectedId)
            }
            is DashboardEvent.NavigateToAccounts -> {
                navigator.navigateToAccountsListScreen()
            }
            is DashboardEvent.NavigateToRecords -> {
                navigator.navigateToRecordsListScreen(it.filter)
            }
            is DashboardEvent.NavigateToCreateRecord -> {
                navigator.navigateToCreateRecordScreen(it.recordType)
            }
        }
    }

    DashboardContent(
        state = state,
        drawerState = drawerState,
        scope = scope,
        addRecordSheetState = addRecordSheetState,
        scrollState = scrollState,
        accountsListState = accountsListState,
        widgetsListState = widgetsListState,
        onAction = vm::onAction
    )
}
