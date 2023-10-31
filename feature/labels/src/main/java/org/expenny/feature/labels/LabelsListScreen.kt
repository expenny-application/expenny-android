package org.expenny.feature.labels

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.extensions.observeWithLifecycle
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.feature.labels.navigation.LabelsListNavArgs
import org.expenny.feature.labels.navigation.LabelsListNavigator
import org.expenny.feature.labels.view.LabelsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = LabelsListNavArgs::class)
@Composable
fun LabelsListScreen(
    navigator: LabelsListNavigator,
    snackbarManager: ExpennySnackbarManager,
    resultNavigator: ResultBackNavigator<LongArrayNavArg>
) {
    val vm: LabelsListViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val lazyListState = rememberLazyListState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBackWithResult -> resultNavigator.navigateBack(it.selection)
            is Event.NavigateBack -> navigator.navigateBack()
            is Event.ShowMessage -> {
                snackbarManager.showMessage(it.message)
            }
            is Event.NavigateToEditLabel -> {
                navigator.navigateToEditLabelScreen(it.id)
            }
            is Event.NavigateToCreateLabel -> {
                navigator.navigateToCreateLabelScreen()
            }
        }
    }

    LabelsListContent(
        state = state,
        lazyListState = lazyListState,
        onAction = vm::onAction
    )
}