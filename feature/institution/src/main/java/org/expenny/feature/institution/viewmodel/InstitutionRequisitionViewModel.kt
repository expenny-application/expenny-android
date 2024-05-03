package org.expenny.feature.institution.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.domain.usecase.institution.CreateInstitutionRequisitionUseCase
import org.expenny.core.domain.usecase.institution.DeleteInstitutionRequisitionUseCase
import org.expenny.core.model.resource.RemoteResult
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.institution.contract.InstitutionRequisitionAction
import org.expenny.feature.institution.contract.InstitutionRequisitionEvent
import org.expenny.feature.institution.contract.InstitutionRequisitionEvent.ShowMessage
import org.expenny.feature.institution.contract.InstitutionRequisitionEvent.ShowError
import org.expenny.feature.institution.contract.InstitutionRequisitionEvent.NavigateBack
import org.expenny.feature.institution.contract.InstitutionRequisitionEvent.NavigateToBackToAccountsList
import org.expenny.feature.institution.contract.InstitutionRequisitionState
import org.expenny.feature.institution.navArgs
import org.expenny.feature.institution.navigation.InstitutionRequisitionNavArgs
import org.expenny.core.resources.R
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InstitutionRequisitionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createInstitutionRequisition: CreateInstitutionRequisitionUseCase,
    private val deleteInstitutionRequisition: DeleteInstitutionRequisitionUseCase,
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
            deleteInstitutionRequisition(DeleteInstitutionRequisitionUseCase.Params(requisitionId))
                .collect { postSideEffect(NavigateBack) }
        } else {
            postSideEffect(NavigateBack)
        }
    }

    private fun handleOnRequisitionGranted() = intent {
        postSideEffect(ShowMessage(fromRes(R.string.requisition_granted_message)))
        postSideEffect(NavigateToBackToAccountsList)
    }

    private fun subscribeToRequisition() = intent {
        createInstitutionRequisition(CreateInstitutionRequisitionUseCase.Params(institutionId)).collect {
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