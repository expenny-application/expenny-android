package org.expenny.feature.institution.requisition

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.requisition.CreateRequisitionUseCase
import org.expenny.core.domain.usecase.requisition.DeleteRequisitionUseCase
import org.expenny.core.domain.usecase.requisition.GetRequisitionUseCase
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.model.institution.InstitutionRequisitionStatus.Linked
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionAction
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionEvent
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionEvent.ShowError
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionEvent.NavigateBack
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionEvent.NavigateToBackToAccountsList
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionState
import org.expenny.feature.institution.navArgs
import org.expenny.feature.institution.requisition.navigation.InstitutionRequisitionNavArgs
import org.expenny.core.resources.R
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InstitutionRequisitionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRequisition: GetRequisitionUseCase,
    private val createRequisition: CreateRequisitionUseCase,
    private val deleteRequisition: DeleteRequisitionUseCase,
) : ExpennyViewModel<InstitutionRequisitionAction>(),
    ContainerHost<InstitutionRequisitionState, InstitutionRequisitionEvent> {

    private val institutionId = savedStateHandle.navArgs<InstitutionRequisitionNavArgs>().institutionId
    private var requisitionId: String = ""

    override val container = container<InstitutionRequisitionState, InstitutionRequisitionEvent>(
        initialState = InstitutionRequisitionState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch { subscribeToRequisition() }
        }
    }

    override fun onAction(action: InstitutionRequisitionAction) {
        when (action) {
            is InstitutionRequisitionAction.OnRequisitionAborted -> handleOnRequisitionAborted()
            is InstitutionRequisitionAction.OnRequisitionGranted -> handleOnRequisitionGranted()
        }
    }

    private fun handleOnRequisitionAborted() = intent {
        if (requisitionId.isNotBlank()) {
            deleteRequisition(DeleteRequisitionUseCase.Params(requisitionId)).collect {
                postSideEffect(NavigateBack)
            }
        } else {
            postSideEffect(NavigateBack)
        }
    }

    private fun handleOnRequisitionGranted() = intent {
        getRequisition(GetRequisitionUseCase.Params(requisitionId)).collect {
            when (it) {
                is RemoteResult.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is RemoteResult.Success -> {
                    if (it.data.status == Linked && it.data.accounts.isNotEmpty()) {
                        postSideEffect(NavigateToBackToAccountsList)
                    } else {
                        postSideEffect(ShowError(fromRes(R.string.internal_error)))
                        postSideEffect(NavigateToBackToAccountsList)
                    }
                }
                is RemoteResult.Error -> {
                    postSideEffect(ShowError(parseError(it.throwable)))
                    postSideEffect(NavigateToBackToAccountsList)
                }
            }
        }
    }

    private fun subscribeToRequisition() = intent {
        createRequisition(CreateRequisitionUseCase.Params(institutionId)).collect {
            when (it) {
                is RemoteResult.Loading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is RemoteResult.Success -> {
                    requisitionId = it.data.id
                    reduce {
                        state.copy(
                            isLoading = false,
                            url = it.data.url,
                            redirectUrl = it.data.redirectUrl,
                        )
                    }
                }
                is RemoteResult.Error -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ShowError(parseError(it.throwable)))
                }
            }
        }
    }
}