package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import org.expenny.core.common.extensions.isZero
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.common.DatedAmount
import org.expenny.core.model.record.Record
import javax.inject.Inject

class GetAccountBalanceHistoryUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<List<DatedAmount>> {
        return combine(
            accountRepository.getAccount(params.accountId).filterNotNull(),
            recordRepository.getRecordsAsc()
        ) { account, records ->
            val sortedAccountRecords = records
                .filterIsInstance<Record.Transaction>()
                .filter { it.account.id == params.accountId }

            // Base condition
            if (sortedAccountRecords.isEmpty()) {
                val initialBalance = DatedAmount(account.startBalance.value, account.createdAt.toLocalDate())
                return@combine listOf(initialBalance)
            }

            // Typed records amounts to calculated the balance trend
            val recordTypedAmounts = sortedAccountRecords
                .groupBy { it.date.toLocalDate() }
                .map { entry ->
                    val dateAmount = entry.value.sumOf { it.typedAmount.value }
                    DatedAmount(dateAmount, entry.key)
                }

            val initialDate = recordTypedAmounts.first().date
            val initialBalanceValue = account.startBalance.value
            val initialBalance = DatedAmount(initialBalanceValue, initialDate)

            val balanceTrend = recordTypedAmounts
                .runningFold(initialBalance) { previous, current ->
                    val balanceAmount = previous.amount + current.amount
                    current.copy(balanceAmount)
                }

            return@combine balanceTrend
        }
    }

    data class Params(
        val accountId: Long
    )
}