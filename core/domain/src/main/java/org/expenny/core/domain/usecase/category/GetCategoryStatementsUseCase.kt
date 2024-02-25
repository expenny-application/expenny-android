package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.category.CategoryStatement
import org.expenny.core.model.record.Record
import org.threeten.extra.LocalDateRange
import javax.inject.Inject

class GetCategoryStatementsUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<List<CategoryStatement>> {
        val accountId = params.accountId
        val dateRange = params.dateRange

        return recordRepository.getRecordsDesc().map { records ->
            val accountRecords = records
                .filterIsInstance<Record.Transaction>()
                .filter {
                    val accountCondition = if (accountId != null) it.account.id == accountId else true
                    val dateRangeCondition = dateRange.contains(it.date.toLocalDate())
                    return@filter accountCondition && dateRangeCondition
                }

            val listOfCategoryStatements = accountRecords
                .groupBy { it.category }
                .map { (category, transactions) ->
                    val amountSum = transactions
                        .map { it.typedAmount }
                        .reduce { acc, amount ->
                            amount.copy(acc.value + amount.value)
                        }

                    CategoryStatement(
                        amount = amountSum,
                        transactions = transactions,
                        category = category
                    )
                }

            return@map listOfCategoryStatements
        }
    }

    data class Params(
        val accountId: Long? = null,
        val dateRange: LocalDateRange
    )
}