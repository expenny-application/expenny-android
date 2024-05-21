package org.expenny.feature.records.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.feature.records.list.contract.RecordsListEvent
import org.expenny.feature.records.list.navigation.RecordsListNavArgs
import org.expenny.feature.records.list.navigation.RecordsListNavigator
import org.expenny.feature.records.list.view.RecordsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = RecordsListNavArgs::class)
@Composable
fun RecordsListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: RecordsListNavigator,
    drawerState: ExpennyDrawerManager
) {
    val vm: RecordsListViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val actionsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    vm.collectSideEffect {
        when(it) {
            is RecordsListEvent.NavigateBack -> {
                navigator.navigateBack()
            }
            is RecordsListEvent.NavigateToCreateRecord -> {
                navigator.navigateToCreateRecordScreen()
            }
            is RecordsListEvent.NavigateToEditRecord -> {
                navigator.navigateToEditRecordScreen(it.id)
            }
            is RecordsListEvent.NavigateToCloneRecord -> {
                navigator.navigateToCloneRecordScreen(it.id)
            }
            is RecordsListEvent.ShowMessage -> {
                snackbarManager.showInfo(it.message)
            }
        }
    }

    RecordsListContent(
        state = state,
        scope = scope,
        recordActionsSheetState = actionsSheetState,
        drawerState = drawerState,
        lazyListState = lazyListState,
        onAction = vm::onAction
    )
}
