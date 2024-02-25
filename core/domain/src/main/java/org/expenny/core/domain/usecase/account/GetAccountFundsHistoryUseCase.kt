package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.types.TransactionType
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.common.DatedAmount
import org.expenny.core.model.record.Record
import javax.inject.Inject

class GetAccountFundsHistoryUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<List<DatedAmount>> {
        return recordRepository.getRecordsAsc().map { records ->
            val sortedAccountRecords = records
                .filterIsInstance<Record.Transaction>()
                .filter { it.account.id == params.accountId && it.type == params.transactionType }

            if (records.isEmpty()) {
                return@map emptyList()
            }

            val fundsTrend = sortedAccountRecords
                .groupBy { it.date.toLocalDate() }
                .map { entry ->
                    val dateAmount = entry.value.sumOf { it.amount.value }
                    DatedAmount(dateAmount, entry.key)
                }.run {
                    if (params.isAccumulated) {
                        runningReduce { previous, current ->
                            val accumulativeAmount = previous.amount + current.amount
                            current.copy(accumulativeAmount)
                        }
                    } else this
                }

            return@map fundsTrend
        }
    }

    data class Params(
        val accountId: Long,
        val transactionType: TransactionType,
        val isAccumulated: Boolean
    )
}