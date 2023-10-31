package org.expenny.feature.records

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.utils.ExpennyDrawerState
import org.expenny.feature.records.model.*
import org.expenny.feature.records.navigation.RecordsListNavArgs
import org.expenny.feature.records.navigation.RecordsListNavigator
import org.expenny.feature.records.view.RecordsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = RecordsListNavArgs::class)
@Composable
fun RecordsListScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: RecordsListNavigator,
    drawerState: ExpennyDrawerState
) {
    val vm: RecordsListViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val actionsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    vm.collectSideEffect {
        when(it) {
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.NavigateToCreateRecord -> {
                navigator.navigateToCreateRecordScreen()
            }
            is Event.NavigateToEditRecord -> {
                navigator.navigateToEditRecordScreen(it.id)
            }
            is Event.NavigateToCloneRecord -> {
                navigator.navigateToCloneRecordScreen(it.id)
            }
            is Event.ShowMessage -> {
                snackbarManager.showMessage(it.message)
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
