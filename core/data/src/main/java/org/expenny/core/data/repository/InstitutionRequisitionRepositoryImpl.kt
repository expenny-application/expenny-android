package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionRequisitionRepository
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.network.GoCardlessService
import javax.inject.Inject

class InstitutionRequisitionRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionRequisitionRepository {

    override fun createInstitutionRequisition(institutionId: String): Flow<RemoteResult<InstitutionRequisition>> {
        return remoteResultMediator {
            goCardlessService.createInstitutionRequisition(institutionId).toModel()
        }
    }

    override fun getInstitutionRequisition(requisitionId: String): Flow<RemoteResult<InstitutionRequisition>> {
        return remoteResultMediator {
            goCardlessService.getInstitutionRequisition(requisitionId).toModel()
        }
    }

    override fun deleteInstitutionRequisition(requisitionId: String): Flow<RemoteResult<Boolean>> {
        return remoteResultMediator {
            goCardlessService.deleteInstitutionRequisition(requisitionId)
        }
    }
}