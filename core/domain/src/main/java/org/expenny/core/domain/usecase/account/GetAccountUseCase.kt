package org.expenny.core.domain.usecase.account

import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.Account
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(params: Params): Account? {
        return accountRepository.getAccount(params.id)
    }

    data class Params(
        val id: Long
    )
}