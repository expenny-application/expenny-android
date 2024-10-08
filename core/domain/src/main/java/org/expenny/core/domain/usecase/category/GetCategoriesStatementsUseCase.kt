package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.types.TransactionType
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.category.CategoryStatement
import org.expenny.core.model.record.Record
import java.time.LocalDateTime
import javax.inject.Inject

class GetCategoriesStatementsUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(
        accountIds: List<Long> = emptyList(),
        dateTimeRange: ClosedRange<LocalDateTime>,
        transactionType: TransactionType? = null
    ): Flow<List<CategoryStatement>> {
        return recordRepository.getRecordsDesc().map { records ->
            val accountRecords = records
                .filterIsInstance<Record.Transaction>()
                .filter {
                    val transactionTypeCondition = if (transactionType != null) it.type == transactionType else true
                    val accountsCondition = if (accountIds.isNotEmpty()) it.account.id in accountIds else true
                    val dateRangeCondition = dateTimeRange.contains(it.date)

                    return@filter transactionTypeCondition && accountsCondition && dateRangeCondition
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
}