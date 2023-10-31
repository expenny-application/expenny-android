package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.account.AccountWithRecords
import org.expenny.core.model.record.Record
import javax.inject.Inject

class GetAccountsWithRecordsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<List<AccountWithRecords>> {
        return combine(
            accountRepository.getAccountsFlow(),
            recordRepository.getRecordsDesc()
        ) { accounts, records ->
            accounts.asSequence()
                .filter { if (params.accountIds.isNotEmpty()) it.id in params.accountIds else true }
                .toList()
                .map { account ->
                    val accountRecords = records.filter {
                        if (it is Record.Transfer) {
                            it.transferAccount.id == account.id || it.account.id == account.id
                        } else {
                            it.account.id == account.id
                        }
                    }

                    AccountWithRecords(account, accountRecords)
                }
        }
    }

    operator fun invoke(): Flow<List<AccountWithRecords>> {
        return invoke(Params())
    }

    data class Params(
        val accountIds: List<Long> = listOf()
    )
}