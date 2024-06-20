package org.expenny.feature.records.labels

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.containsIgnoreCase
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.record.GetRecordLabelsUseCase
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.ui.data.navargs.StringArrayNavArg
import org.expenny.feature.records.labels.contract.RecordLabelsListAction
import org.expenny.feature.records.labels.contract.RecordLabelsListEvent
import org.expenny.feature.records.labels.contract.RecordLabelsListState
import org.expenny.feature.records.labels.navigation.RecordLabelsListNavArgs
import org.expenny.feature.records.navArgs
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RecordLabelsListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecordLabels: GetRecordLabelsUseCase,
) : ExpennyViewModel<RecordLabelsListAction>(), ContainerHost<RecordLabelsListState, RecordLabelsListEvent>  {

    private val maxLabelsCount = 5
    private var labels = emptyList<String>()

    private val state get() = container.stateFlow.value

    override val container = container<RecordLabelsListState, RecordLabelsListEvent>(
        initialState = RecordLabelsListState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setInitialData()
            launch { subscribeToLabels() }
        }
    }

    override fun onAction(action: RecordLabelsListAction) {
        when (action) {
            is RecordLabelsListAction.OnSearchQueryChange -> handleOnSearchQueryChange(action)
            is RecordLabelsListAction.OnLabelAdd -> handleOnLabelAdd(action)
            is RecordLabelsListAction.OnLabelRemove -> handleOnLabelRemove(action)
            is RecordLabelsListAction.OnConfirmClick -> handleOnConfirmClick()
            is RecordLabelsListAction.OnCloseClick -> handleOnCloseClick()
        }
    }

    private fun handleOnSearchQueryChange(action: RecordLabelsListAction.OnSearchQueryChange) {
        blockingIntent {
            reduce {
                state.copy(searchQuery = action.query)
            }
        }
    }

    private fun handleOnLabelRemove(action: RecordLabelsListAction.OnLabelRemove) = intent {
        reduce {
            state.copy(selectedLabels = state.selectedLabels - action.label)
        }
    }

    private fun handleOnLabelAdd(action: RecordLabelsListAction.OnLabelAdd) {
        if (!state.selectedLabels.containsIgnoreCase(action.label)) {
            val selectedLabels = state.selectedLabels + action.label.trim()

            if (selectedLabels.size <= maxLabelsCount) {
                intent {
                    reduce {
                        state.copy(
                            selectedLabels = selectedLabels,
                            searchQuery = ""
                        )
                    }
                }
            } else {
                intent {
                    postSideEffect(RecordLabelsListEvent.ShowError(fromRes(R.string.max_labels_amount_error, maxLabelsCount)))
                }
            }
        } else {
            intent {
                postSideEffect(RecordLabelsListEvent.ShowError(fromRes(R.string.duplicate_data_error)))
            }
        }
    }

    private fun handleOnCloseClick() = intent {
        postSideEffect(RecordLabelsListEvent.NavigateBack)
    }

    private fun handleOnConfirmClick() = intent {
        val result = StringArrayNavArg(state.selectedLabels.toTypedArray())
        postSideEffect(RecordLabelsListEvent.NavigateBackWithResult(result))
    }

    private fun setInitialData() {
        savedStateHandle.navArgs<RecordLabelsListNavArgs>().also { args ->
            intent {
                val selectedLabels = args.selection.values.toList()
                val labels = getRecordLabels().first()

                reduce {
                    state.copy(
                        labels = labels,
                        selectedLabels = selectedLabels,
                        showSearch = true
                    )
                }
            }
        }
    }

    private fun subscribeToLabels() = intent {
        repeatOnSubscription {
            getRecordLabels().collect {
                labels = it
            }
        }
    }
}