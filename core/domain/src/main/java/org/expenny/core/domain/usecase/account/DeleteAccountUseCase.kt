package org.expenny.core.domain.usecase.account

import org.expenny.core.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(accountId: Long) {
        accountRepository.deleteAccount(accountId)
    }
}