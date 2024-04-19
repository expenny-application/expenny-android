@file:OptIn(OrbitExperimental::class)

package org.expenny.feature.categorydetails

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.expenny.core.common.utils.ResourceIdProvider
import org.expenny.core.common.utils.ResourceNameProvider
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.category.CreateCategoryUseCase
import org.expenny.core.domain.usecase.category.DeleteCategoryUseCase
import org.expenny.core.domain.usecase.category.GetCategoryUseCase
import org.expenny.core.domain.usecase.category.UpdateCategoryUseCase
import org.expenny.core.domain.validators.MinimumLengthValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.model.category.Category
import org.expenny.core.resources.R
import org.expenny.feature.categorydetails.navigation.CategoryDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCategory: GetCategoryUseCase,
    private val createCategory: CreateCategoryUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val deleteCategory: DeleteCategoryUseCase,
    private val validateInput: ValidateInputUseCase,
    private val resourceNameProvider: ResourceNameProvider,
    private val resourceIdProvider: ResourceIdProvider,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private val currentCategory = MutableStateFlow<Category?>(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setupInitialState()
        }
    }

    override fun onAction(action: Action) {
        when(action) {
            is Action.OnNameChange -> handleOnNameChange(action)
            is Action.OnColorChange -> handleOnColorChange(action)
            is Action.OnIconChange -> handleOnIconChange(action)
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnDeleteClick -> handleOnDeleteClick()
            is Action.OnDeleteRecordDialogDismiss -> handleOnDeleteRecordDialogDismiss()
            is Action.OnDeleteRecordDialogConfirm -> handleOnDeleteRecordDialogConfirm()
        }
    }

    private fun handleOnNameChange(action: Action.OnNameChange) = blockingIntent {
        reduce {
            state.copy(
                nameInput = state.nameInput.copy(
                    value = action.name,
                    error = validateName(action.name).errorRes
                )
            )
        }
    }

    private fun handleOnColorChange(action: Action.OnColorChange) = intent {
        reduce {
            state.copy(selectedColor = action.color)
        }
    }

    private fun handleOnIconChange(action: Action.OnIconChange) = intent {
        reduce {
            state.copy(selectedIconResId = action.iconResId)
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            val selectedIconResName = resourceNameProvider(state.selectedIconResId)!!

            if (currentCategory.value != null) {
                updateCategory(
                    UpdateCategoryUseCase.Params(
                        id = currentCategory.value!!.id,
                        name = state.nameInput.value,
                        iconResName = selectedIconResName,
                        colorArgb = state.selectedColor.toArgb()
                    )
                )
            } else {
                createCategory(
                    CreateCategoryUseCase.Params(
                        name = state.nameInput.value,
                        iconResName = selectedIconResName,
                        colorArgb = state.selectedColor.toArgb()
                    )
                )
            }
            postSideEffect(Event.ShowMessage(StringResource.fromRes(R.string.saved_message)))
            postSideEffect(Event.NavigateBack)
        }
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(showDeleteDialog = true) }
    }

    private fun handleOnDeleteRecordDialogDismiss() = intent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        reduce { state.copy(showDeleteDialog = false) }

        deleteCategory(currentCategory.value!!.id)
        postSideEffect(Event.ShowMessage(StringResource.fromRes(R.string.deleted_message)))
        postSideEffect(Event.NavigateBack)
    }

    private fun setupInitialState() {
        savedStateHandle.navArgs<CategoryDetailsNavArgs>().also { args ->
            intent {
                if (args.categoryId != null) {
                    val category = getCategory(GetCategoryUseCase.Params(args.categoryId))!!
                    val categoryIconResId = resourceIdProvider(category.iconResName)!!
                    currentCategory.value = category

                    reduce {
                        state.copy(
                            toolbarTitle = StringResource.fromRes(R.string.edit_category_label),
                            nameInput = state.nameInput.copy(value = category.name),
                            selectedColor = Color(category.colorArgb),
                            selectedIconResId = categoryIconResId,
                            showDeleteButton = true,
                        )
                    }
                } else {
                    reduce {
                        state.copy(toolbarTitle = StringResource.fromRes(R.string.add_category_label))
                    }
                    postSideEffect(Event.RequestNameInputFocus)
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        val nameValidationResult = validateName(container.stateFlow.value.nameInput.value)

        intent {
            reduce {
                state.copy(nameInput = state.nameInput.copy(error = nameValidationResult.errorRes))
            }
        }

        return nameValidationResult.isValid
    }

    private fun validateName(value: String) = validateInput(
        value, listOf(RequiredStringValidator(), MinimumLengthValidator(2))
    )
}