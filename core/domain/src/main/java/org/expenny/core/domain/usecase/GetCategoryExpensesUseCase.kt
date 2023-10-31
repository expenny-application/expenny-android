package org.expenny.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TimePeriod
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.record.Record
import org.expenny.core.model.expenses.CategoryExpenses
import org.expenny.core.model.expenses.CategoryExpensesData
import java.math.BigDecimal
import javax.inject.Inject

class GetCategoryExpensesUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    operator fun invoke(params: Params): Flow<CategoryExpensesData> {
        val currency = params.currency

        fun filterByAccounts(record: Record): Boolean {
            return params.accountIds.takeIf { it.isNotEmpty() }?.let { record.account.id in it } ?: true
        }
        fun filterByTimeSpan(record: Record): Boolean {
            return params.timePeriod?.toLocalDateRange()?.contains(record.date.toLocalDate()) ?: true
        }

        return recordRepository.getRecordsDesc().map { records ->
            val categoryExpenses = records.asSequence()
                .filterIsInstance(Record.Transaction::class.java)
                .filter { it.recordType == RecordType.Expense && filterByAccounts(it) && filterByTimeSpan(it) }
                .groupBy { it.category }
                .map { categoryToExpenses ->
                    val categoryExpensesValues = categoryToExpenses.value.mapToConvertedAmountValue(currency)
                    val categoryExpensesAmount = CurrencyAmount(currency, categoryExpensesValues.sumByDecimal { it })

                    CategoryExpenses(
                        amount = categoryExpensesAmount,
                        expensesCount = categoryExpensesValues.size,
                        category = categoryToExpenses.key
                    )
                }.sortedBy { it.amount.value }

            val totalAmount = categoryExpenses.sumByDecimal { it.amount.value }

            return@map CategoryExpensesData(
                totalAmount = CurrencyAmount(currency, totalAmount),
                expenses = categoryExpenses
            )
        }
    }

    private fun List<Record.Transaction>.mapToConvertedAmountValue(currency: Currency): List<BigDecimal> {
        return map { record ->
            record.amount.convertTo(currency).value.abs() // apply modulo for expenses
        }
    }

    data class Params(
        val currency: Currency,
        val timePeriod: TimePeriod? = null,
        val accountIds: List<Long> = listOf()
    )
}