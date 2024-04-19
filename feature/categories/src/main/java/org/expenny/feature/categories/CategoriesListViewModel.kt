package org.expenny.feature.categories

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.category.GetCategoriesUseCase
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.mapper.CategoryMapper
import org.expenny.feature.categories.navigation.CategoriesListNavArgs
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
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToCategories() }
            launch { setupInitialState() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnAddCategoryClick -> handleOnAddCategoryClick()
            is Action.OnCategoryClick -> handleOnCategoryClick(action)
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnAddCategoryClick() = intent {
        postSideEffect(Event.NavigateToCreateCategory)
    }

    private fun handleOnCategoryClick(action: Action.OnCategoryClick) = intent {
        if (state.selection != null) {
            postSideEffect(Event.NavigateBackWithResult(LongNavArg(action.id)))
        } else {
            postSideEffect(Event.NavigateToEditCategory(action.id))
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
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