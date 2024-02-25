package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    operator fun invoke(params: Params = Params()): Flow<List<Account>> {
        return accountRepository.getAccounts().map { accounts ->
            if (params.accountIds.isNotEmpty()) {
                accounts.filter { it.id in params.accountIds }
            } else {
                accounts
            }
        }
    }

    data class Params(
        val accountIds: List<Long> = emptyList()
    )
}