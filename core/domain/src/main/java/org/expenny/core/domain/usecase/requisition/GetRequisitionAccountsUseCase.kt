package org.expenny.core.domain.usecase.requisition

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import org.expenny.core.domain.repository.InstitutionAccountRepository
import org.expenny.core.domain.repository.InstitutionRequisitionRepository
import org.expenny.core.model.institution.InstitutionAccount
import org.expenny.core.common.utils.RemoteResult
import javax.inject.Inject

class GetRequisitionAccountsUseCase @Inject constructor(
    private val institutionRequisitionRepository: InstitutionRequisitionRepository,
    private val institutionAccountRepository: InstitutionAccountRepository,
) {

    operator fun invoke(params: Params): Flow<RemoteResult<List<InstitutionAccount>>> {
        return institutionRequisitionRepository.getInstitutionRequisition(params.requisitionId)
            .flatMapConcat { requisitionResult ->
                when (requisitionResult) {
                    is RemoteResult.Success -> {
                        val accounts = requisitionResult.data.accounts

                        if (accounts.isEmpty()) {
                            flowOf(RemoteResult.Success(emptyList()))
                        } else {
                            val accountFlows = accounts.map {
                                institutionAccountRepository.getInstitutionAccount(it)
                            }
                            combineRemoteResult(accountFlows)
                        }
                    }
                    is RemoteResult.Loading -> {
                        flowOf(RemoteResult.Loading)
                    }
                    is RemoteResult.Error -> {
                        flowOf(RemoteResult.Error(requisitionResult.throwable))
                    }
                }
            }
    }

    private inline fun <reified T> combineRemoteResult(
        flows: Iterable<Flow<RemoteResult<T>>>
    ): Flow<RemoteResult<List<T>>> {
        return combine(flows) { results ->
            val errorResults = results.filterIsInstance<RemoteResult.Error>()

            if (errorResults.isNotEmpty()) {
                RemoteResult.Error(errorResults.first().throwable)
            } else {
                val successData = results.mapNotNull {
                    if (it is RemoteResult.Success) it.data else null
                }
                if (successData.isEmpty() || successData.size != results.size) {
                    RemoteResult.Loading
                } else {
                    RemoteResult.Success(successData)
                }
            }
        }
    }

    data class Params(
        val requisitionId: String,
    )
}