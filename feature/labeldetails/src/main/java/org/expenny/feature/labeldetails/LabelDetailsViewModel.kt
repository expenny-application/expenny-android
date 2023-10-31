package org.expenny.feature.labeldetails

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.patrykandpatrick.vico.core.extension.colorHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.expenny.core.common.extensions.asColorHex
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.ValidateInputUseCase
import org.expenny.core.domain.usecase.label.CreateLabelUseCase
import org.expenny.core.domain.usecase.label.DeleteLabelUseCase
import org.expenny.core.domain.usecase.label.GetLabelUseCase
import org.expenny.core.domain.usecase.label.UpdateLabelUseCase
import org.expenny.core.domain.validators.ColorHexValidator
import org.expenny.core.domain.validators.MinimumLengthValidator
import org.expenny.core.domain.validators.RequiredStringValidator
import org.expenny.core.model.label.Label
import org.expenny.core.resources.R
import org.expenny.feature.labeldetails.navigation.LabelDetailsNavArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LabelDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getLabel: GetLabelUseCase,
    private val createLabel: CreateLabelUseCase,
    private val updateLabel: UpdateLabelUseCase,
    private val deleteLabel: DeleteLabelUseCase,
    private val validateInput: ValidateInputUseCase,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    private val currentLabel = MutableStateFlow<Label?>(null)

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setNavArgs()
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnNameChange -> handleOnNameChange(action)
            is Action.OnColorChange -> handleOnColorChange(action)
            is Action.OnGenerateRandomColorClick -> handleOnGenerateRandomColorClick()
            is Action.OnSaveClick -> handleOnSaveClick()
            is Action.OnDeleteClick -> handleOnDeleteClick()
            is Action.OnDeleteDialogDismiss -> handleOnDeleteRecordDialogDismiss()
            is Action.OnDeleteDialogConfirm -> handleOnDeleteRecordDialogConfirm()
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    @SuppressLint("Range")
    private fun handleOnSaveClick() = intent {
        if (validateFields()) {
            if (currentLabel.value == null) {
                createLabel(
                    CreateLabelUseCase.Params(
                        name = state.nameInput.value,
                        colorArgb = state.colorArgb
                    )
                )
            } else {
                updateLabel(
                    UpdateLabelUseCase.Params(
                        id = currentLabel.value!!.id,
                        name = state.nameInput.value,
                        colorArgb = state.colorArgb
                    )
                )
            }
            postSideEffect(Event.NavigateBack)
        }
    }

    private fun setNavArgs() {
        savedStateHandle.navArgs<LabelDetailsNavArgs>().also { args ->
            intent {
                if (args.labelId != null) {
                    val label = getLabel(GetLabelUseCase.Params(args.labelId))!!
                    currentLabel.value = label

                    reduce {
                        state.copy(
                            toolbarTitle = StringResource.fromRes(R.string.edit_label_label),
                            nameInput = state.nameInput.copy(value = label.name),
                            colorArgb = label.colorArgb,
                            showDeleteButton = true,
                        )
                    }
                } else {
                    reduce {
                        state.copy(toolbarTitle = StringResource.fromRes(R.string.add_label_label))
                    }
                    handleOnGenerateRandomColorClick()
                }
            }
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnDeleteClick() = intent {
        reduce { state.copy(showDeleteDialog = true) }
    }

    private fun handleOnDeleteRecordDialogDismiss() = intent {
        reduce { state.copy(showDeleteDialog = false) }
    }

    private fun handleOnDeleteRecordDialogConfirm() = intent {
        reduce { state.copy(showDeleteDialog = false) }

        deleteLabel(currentLabel.value!!.id)
        postSideEffect(Event.ShowMessage(StringResource.fromRes(R.string.deleted_message)))
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnNameChange(action: Action.OnNameChange) = intent {
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
            state.copy(
                colorInput = state.colorInput.copy(value = action.colorArgb.asColorHex),
                colorArgb = action.colorArgb
            )
        }
    }

    private fun handleOnGenerateRandomColorClick() {
        val colorArgb = Random.Default.let {
            Color.rgb(it.nextInt(256), it.nextInt(256), it.nextInt(256))
        }
        intent {
            reduce {
                state.copy(
                    colorInput = state.colorInput.copy(value = colorArgb.colorHex),
                    colorArgb = colorArgb
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val nameValidationResult = validateName(container.stateFlow.value.nameInput.value)
        val colorValidationResult = validateColor(container.stateFlow.value.colorInput.value)

        intent {
            reduce {
                state.copy(
                    nameInput = state.nameInput.copy(error = nameValidationResult.errorRes),
                    colorInput = state.colorInput.copy(error = colorValidationResult.errorRes),
                )
            }
        }

        return listOf(nameValidationResult, colorValidationResult).all { it.isValid }
    }

    private fun validateName(value: String) = validateInput(
        value, listOf(RequiredStringValidator(), MinimumLengthValidator(2))
    )

    private fun validateColor(value: String) = validateInput(
        value, listOf(RequiredStringValidator(), ColorHexValidator())
    )
}