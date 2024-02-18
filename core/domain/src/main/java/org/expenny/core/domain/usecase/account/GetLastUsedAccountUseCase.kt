package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.account.Account
import javax.inject.Inject

class GetLastUsedAccountUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository
) {
    
    suspend operator fun invoke(): Account? {
        val lastUsedAccount = recordRepository.getRecordsDesc().first().maxByOrNull { it.id }?.account
        val lastCreatedAccount = accountRepository.getAccounts().first().maxByOrNull { it.id }
        return lastUsedAccount ?: lastCreatedAccount
    }
}