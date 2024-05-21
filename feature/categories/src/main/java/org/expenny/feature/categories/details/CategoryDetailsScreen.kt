package org.expenny.feature.categories.details

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.feature.categories.details.contract.CategoryDetailsEvent
import org.expenny.feature.categories.details.navigation.CategoryDetailsNavArgs
import org.expenny.feature.categories.details.navigation.CategoryDetailsNavigator
import org.expenny.feature.categories.details.view.CategoryDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = CategoryDetailsNavArgs::class)
@Composable
fun CategoryDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: CategoryDetailsNavigator,
) {
    val vm: CategoryDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val nameInputFocusRequester = remember { FocusRequester() }

    vm.collectSideEffect {
        when (it) {
            is CategoryDetailsEvent.RequestNameInputFocus -> {
                nameInputFocusRequester.requestFocus()
            }
            is CategoryDetailsEvent.NavigateBack -> {
                navigator.navigateBack()
            }
            is CategoryDetailsEvent.ShowMessage -> {
                snackbarManager.showInfo(it.message)
            }
        }
    }

    CategoryDetailsContent(
        state = state,
        scrollState = scrollState,
        nameInputFocusRequester = nameInputFocusRequester,
        onAction = vm::onAction
    )
}