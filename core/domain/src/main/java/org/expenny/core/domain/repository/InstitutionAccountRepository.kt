package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.institution.InstitutionAccount
import org.expenny.core.common.utils.RemoteResult

interface InstitutionAccountRepository {

    fun getInstitutionAccount(accountId: String): Flow<RemoteResult<InstitutionAccount>>
}