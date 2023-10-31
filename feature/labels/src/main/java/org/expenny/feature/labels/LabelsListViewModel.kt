package org.expenny.feature.labels

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.toggleItem
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.viewmodel.*
import org.expenny.core.domain.usecase.label.GetLabelsUseCase
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.mapper.LabelMapper
import org.expenny.feature.labels.navigation.LabelsListNavArgs
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class LabelsListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getLabels: GetLabelsUseCase,
    private val labelMapper: LabelMapper
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setNavArgs()
            launch { subscribeToLabels() }
        }
    }
    
    override fun onAction(action: Action) {
        when (action) {
            is Action.OnLabelClick -> handleOnLabelClick(action)
            is Action.OnConfirmClick -> handleOnConfirmClick()
            is Action.OnBackClick -> handleOnBackClick()
            is Action.OnAddLabelClick -> handleOnAddLabelClick()
        }
    }

    private fun setNavArgs() {
        savedStateHandle.navArgs<LabelsListNavArgs>().also { args ->
            if (args.selection != null) {
                intent {
                    reduce {
                        state.copy(
                            toolbarTitle = StringResource.fromRes(R.string.select_labels_label),
                            selection = MultiSelection(args.selection.values.toList())
                        )
                    }
                }
            }
        }
    }

    private fun handleOnAddLabelClick() = intent {
        postSideEffect(Event.NavigateToCreateLabel)
    }

    private fun handleOnLabelClick(action: Action.OnLabelClick) {
        intent {
            when (val selection = state.selection) {
                is MultiSelection -> {
                    val newSelectionData = selection.data.toggleItem(action.id)
                    reduce {
                        state.copy(selection = MultiSelection(data = newSelectionData))
                    }
                }
                else -> {
                    postSideEffect(Event.NavigateToEditLabel(action.id))
                }
            }
        }
    }

    private fun subscribeToLabels() = intent {
        repeatOnSubscription {
            getLabels().collect {
                reduce {
                    state.copy(
                        labels = labelMapper(it).toImmutableList(),
                        showConfirmButton = state.selection != null && it.isNotEmpty()
                    )
                }
            }
        }
    }

    private fun handleOnConfirmClick() = intent {
        val results = (state.selection as MultiSelection<Long>).data.toLongArray()
        postSideEffect(Event.NavigateBackWithResult(LongArrayNavArg(results)))
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }
}