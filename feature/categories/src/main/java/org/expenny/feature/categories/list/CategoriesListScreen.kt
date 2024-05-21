package org.expenny.feature.categories.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.feature.categories.list.contract.CategoriesListEvent
import org.expenny.feature.categories.list.navigation.CategoriesListNavArgs
import org.expenny.feature.categories.list.navigation.CategoriesListNavigator
import org.expenny.feature.categories.list.view.CategoriesListContent
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
            is CategoriesListEvent.NavigateBackWithResult -> resultNavigator.navigateBack(it.selection)
            is CategoriesListEvent.NavigateToEditCategory -> navigator.navigateToEditCategoryScreen(it.id)
            is CategoriesListEvent.NavigateToCreateCategory -> navigator.navigateToAddCategoryScreen()
            is CategoriesListEvent.NavigateBack -> navigator.navigateBack()
        }
    }

    CategoriesListContent(
        state = state,
        onAction = vm::onAction
    )
}