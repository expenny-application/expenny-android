package org.expenny.feature.labeldetails

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.common.ExpennySnackbarManager
import org.expenny.core.ui.extensions.observeWithLifecycle
import org.expenny.feature.labeldetails.navigation.LabelDetailsNavArgs
import org.expenny.feature.labeldetails.navigation.LabelDetailsNavigator
import org.expenny.feature.labeldetails.view.LabelDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = LabelDetailsNavArgs::class)
@Composable
fun LabelDetailsScreen(
    navigator: LabelDetailsNavigator,
    snackbarManager: ExpennySnackbarManager
) {
    val vm: LabelDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.ShowMessage -> {
                snackbarManager.showMessage(it.message)
            }
        }
    }

    LabelDetailsContent(
        state = state,
        scrollState = scrollState,
        onAction = vm::onAction
    )
}