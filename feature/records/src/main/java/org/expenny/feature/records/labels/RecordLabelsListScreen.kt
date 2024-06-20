package org.expenny.feature.records.labels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.StringArrayNavArg
import org.expenny.core.ui.transitions.VerticalSlideTransitionStyle
import org.expenny.feature.records.labels.contract.RecordLabelsListEvent
import org.expenny.feature.records.labels.navigation.RecordLabelsListNavArgs
import org.expenny.feature.records.labels.view.RecordLabelsListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(
    navArgsDelegate = RecordLabelsListNavArgs::class,
    style = VerticalSlideTransitionStyle::class
)
@Composable
fun RecordLabelsListScreen(
    snackbarManager: ExpennySnackbarManager,
    resultNavigator: ResultBackNavigator<StringArrayNavArg>
) {
    val vm: RecordLabelsListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is RecordLabelsListEvent.NavigateBackWithResult -> resultNavigator.navigateBack(it.result)
            is RecordLabelsListEvent.NavigateBack -> resultNavigator.navigateBack()
            is RecordLabelsListEvent.ShowError -> snackbarManager.showError(it.error)
        }
    }

    RecordLabelsListContent(
        state = state,
        onAction = vm::onAction,
    )
}