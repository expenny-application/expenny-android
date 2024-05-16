package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.common.utils.RemoteResult

interface InstitutionRequisitionRepository {

    fun createInstitutionRequisition(institutionId: String): Flow<RemoteResult<InstitutionRequisition>>

    fun getInstitutionRequisition(requisitionId: String): Flow<RemoteResult<InstitutionRequisition>>

    fun deleteInstitutionRequisition(requisitionId: String): Flow<RemoteResult<Boolean>>
}