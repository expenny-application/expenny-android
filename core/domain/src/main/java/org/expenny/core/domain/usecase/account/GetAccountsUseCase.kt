package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    operator fun invoke(): Flow<List<Account>> {
        return accountRepository.getAccounts()
    }
}