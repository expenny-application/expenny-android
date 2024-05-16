package org.expenny.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.extensions.toSystemLocalDateTime
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionAccountRepository
import org.expenny.core.model.institution.InstitutionAccount
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.network.GoCardlessService
import java.time.Instant
import java.util.Currency
import javax.inject.Inject

class InstitutionAccountRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionAccountRepository {

    override fun getInstitutionAccount(accountId: String): Flow<RemoteResult<InstitutionAccount>> {
        return remoteResultMediator {
            val accountAsync = async { goCardlessService.getInstitutionAccount(accountId) }
            val accountDetailsAsync = async { goCardlessService.getInstitutionAccountDetails(accountId) }
            val accountBalancesAsync = async { goCardlessService.getInstitutionAccountBalances(accountId) }
            val account = accountAsync.await()
            val accountDetails = accountDetailsAsync.await()
            val accountBalances = accountBalancesAsync.await()

            InstitutionAccount(
                id = account.id,
                resourceId = accountDetails.resourceId,
                institutionId = account.institutionId,
                status = account.status,
                iban = account.iban,
                currency = Currency.getInstance(accountDetails.currency).toModel(),
                name = accountDetails.name,
                ownerName = account.ownerName,
                balance = accountBalances[0].balanceAmount.amount.toBigDecimal(),
                lastAccessedAt = Instant.parse(account.lastAccessedAt).toSystemLocalDateTime(),
                createdAt = Instant.parse(account.createdAt).toSystemLocalDateTime(),
            )
        }
    }
}