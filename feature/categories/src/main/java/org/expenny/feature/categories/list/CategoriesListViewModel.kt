package org.expenny.feature.categories.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.StringResource
import org.expenny.core.domain.usecase.category.GetCategoriesUseCase
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.mapper.CategoryMapper
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.categories.list.contract.CategoriesListAction
import org.expenny.feature.categories.list.contract.CategoriesListEvent
import org.expenny.feature.categories.list.contract.CategoriesListState
import org.expenny.feature.categories.list.navigation.CategoriesListNavArgs
import org.expenny.feature.categories.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCategories: GetCategoriesUseCase,
    private val categoryMapper: CategoryMapper
) : ExpennyViewModel<CategoriesListAction>(), ContainerHost<CategoriesListState, CategoriesListEvent> {

    override val container = container<CategoriesListState, CategoriesListEvent>(
        initialState = CategoriesListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToCategories() }
            launch { setupInitialState() }
        }
    }

    override fun onAction(action: CategoriesListAction) {
        when (action) {
            is CategoriesListAction.OnAddCategoryClick -> handleOnAddCategoryClick()
            is CategoriesListAction.OnCategoryClick -> handleOnCategoryClick(action)
            is CategoriesListAction.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnAddCategoryClick() = intent {
        postSideEffect(CategoriesListEvent.NavigateToCreateCategory)
    }

    private fun handleOnCategoryClick(action: CategoriesListAction.OnCategoryClick) = intent {
        if (state.selection != null) {
            postSideEffect(CategoriesListEvent.NavigateBackWithResult(LongNavArg(action.id)))
        } else {
            postSideEffect(CategoriesListEvent.NavigateToEditCategory(action.id))
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(CategoriesListEvent.NavigateBack)
    }

    private fun subscribeToCategories() = intent {
        repeatOnSubscription {
            getCategories().collect {
                reduce {
                    state.copy(categories = categoryMapper(it))
                }
            }
        }
    }

    private fun setupInitialState() = intent {
        savedStateHandle.navArgs<CategoriesListNavArgs>().also { args ->
            if (args.selection != null) {
                reduce {
                    state.copy(
                        toolbarTitle = StringResource.fromRes(R.string.select_category_label),
                        selection = SingleSelectionUi(args.selection.value)
                    )
                }
            } else {
                reduce {
                    state.copy(toolbarTitle = StringResource.fromRes(R.string.categories_label))
                }
            }
        }
    }
}