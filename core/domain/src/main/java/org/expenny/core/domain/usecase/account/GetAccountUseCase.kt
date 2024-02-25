package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.Account
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    operator fun invoke(params: Params): Flow<Account?> {
        return accountRepository.getAccount(params.id)
    }

    data class Params(
        val id: Long
    )
}