package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.Institution
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.network.GoCardlessService
import javax.inject.Inject

class InstitutionRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionRepository {

    override fun getInstitutions(countryCode: String?): Flow<RemoteResult<List<Institution>>> {
        return remoteResultMediator {
            goCardlessService.getInstitutions(countryCode).map { it.toModel() }
        }
    }

    override fun getSandboxInstitutions(): Flow<RemoteResult<List<Institution>>> {
        return remoteResultMediator {
            goCardlessService.getInstitutions()
                .filter { it.id.startsWith("SANDBOXFINANCE") }
                .map { it.toModel() }
        }
    }
}