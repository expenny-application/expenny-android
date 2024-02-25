package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.ChronoPeriod
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.category.CategoryStatement
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.model.category.CategoryExpenses
import javax.inject.Inject

class GetCategoryExpensesUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<CategoryExpenses> {
        val currency = params.currency

        return recordRepository.getRecordsDesc().map { records ->
            val categoryExpenses = records.asSequence()
                .filter(params.accountIds, params.chronoPeriod)
                .groupBy { it.category }
                .map { categoryToExpenses ->
                    val categoryExpensesValues = categoryToExpenses.value.mapToConvertedAmountValue(currency)
                    val categoryExpensesValuesSum = categoryExpensesValues.sumByDecimal { it }
                    val categoryExpensesAmount = CurrencyAmount(categoryExpensesValuesSum, currency)

                    CategoryStatement(
                        amount = categoryExpensesAmount,
                        transactions = categoryToExpenses.value,
                        category = categoryToExpenses.key
                    )
                }.sortedBy { it.amount.value }

            val totalAmount = categoryExpenses.sumByDecimal { it.amount.value }

            CategoryExpenses(
                amount = CurrencyAmount(totalAmount, currency),
                expenses = categoryExpenses
            )
        }
    }

    private fun List<Record.Transaction>.mapToConvertedAmountValue(currency: Currency) =
        map { record -> record.amount.convertTo(currency).value }

    private fun Sequence<Record>.filter(accountIds: List<Long>, chronoPeriod: ChronoPeriod?) =
        this.filterIsInstance(Record.Transaction::class.java)
            .filter { record ->
                record.recordType == RecordType.Expense
                        && accountIds.takeIf { it.isNotEmpty() }?.let { record.account.id in it } ?: true
                        && chronoPeriod?.dateTimeRange()?.contains(record.date) ?: true
            }

    data class Params(
        val currency: Currency,
        val chronoPeriod: ChronoPeriod? = null,
        val accountIds: List<Long> = listOf()
    )
}