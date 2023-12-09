package org.expenny.feature.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.categories.navigation.CategoriesListNavArgs
import org.expenny.feature.categories.navigation.CategoriesListNavigator
import org.expenny.feature.categories.view.CategoriesListContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = CategoriesListNavArgs::class)
@Composable
fun CategoriesListScreen(
    navigator: CategoriesListNavigator,
    resultNavigator: ResultBackNavigator<LongNavArg>
) {
    val vm: CategoriesListViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is Event.NavigateBackWithResult -> resultNavigator.navigateBack(it.selection)
            is Event.NavigateToEditCategory -> navigator.navigateToEditCategoryScreen(it.id)
            is Event.NavigateToCreateCategory -> navigator.navigateToAddCategoryScreen()
            is Event.NavigateBack -> navigator.navigateBack()
        }
    }

    CategoriesListContent(
        state = state,
        onAction = vm::onAction
    )
}