package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.data.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionRepository
import org.expenny.core.model.institution.Institution
import org.expenny.core.model.resource.RemoteResult
import org.expenny.core.network.GoCardlessService
import javax.inject.Inject

class InstitutionRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionRepository {

    override fun getInstitutions(countryCode: String): Flow<RemoteResult<List<Institution>>> {
        return remoteResultMediator {
            goCardlessService.getInstitutions(countryCode).map {
                Institution(
                    id = it.id,
                    name = it.name,
                    bic = it.bic,
                    transactionTotalDays = it.transactionTotalDays,
                    logoUrl = it.logoUrl,
                )
            }
        }
    }
}