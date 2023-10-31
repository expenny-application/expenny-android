package org.expenny.feature.accountoverview

import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.isZero
import org.expenny.core.common.extensions.toYearRange
import org.expenny.core.common.types.DateRecurrence
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.usecase.account.GetAccountUseCase
import org.expenny.core.domain.usecase.record.GetRecordsUseCase
import org.expenny.core.model.record.Record
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import org.threeten.extra.LocalDateRange
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.YearMonth
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AccountOverviewViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val getRecords: GetRecordsUseCase
): ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            launch {
                intent {
                    val accountRecords = getRecords(GetRecordsUseCase.Params(accounts = listOf(1))).first()

                    val expensesChartEntries = accountRecords.getMappedData(TransactionType.Outgoing).getChartEntries()
                    val incomeChartEntries = accountRecords.getMappedData(TransactionType.Incoming).getChartEntries()

                    val saldoChartEntries = (expensesChartEntries.toList() + incomeChartEntries.toList())
                        .groupBy({ it.x }, { it.y })
                        .map { (key, values) ->
                            entryOf(key, values.saldo())
                        }

                    reduce {
                        val debitCreditModelProducer = state.debitCreditChartEntryModelProducer.apply { setEntries(
                            listOf(expensesChartEntries, incomeChartEntries)
                        )}
                        val saldoModelProducer = state.saldoChartEntryModelProducer.apply { setEntries(saldoChartEntries) }

                        state.copy(
                            debitCreditChartEntryModelProducer = debitCreditModelProducer,
                            saldoChartEntryModelProducer = saldoModelProducer
                        )
                    }
                }
            }
        }
    }

    private fun Iterable<Float>.saldo(): Float {
        val debitCreditPair = Pair(firstOrNull() ?: 0f, lastOrNull() ?: 0f)
        return debitCreditPair.second - debitCreditPair.first
    }

    private fun Map<LocalDate, BigDecimal>.getChartEntries(): List<FloatEntry> {
        val xValuesToDates = keys.associateBy { it.monthValue.toFloat() }
        return xValuesToDates.keys.zip(values, ::entryOf)
    }

    private fun List<Record>.getMappedData(type: TransactionType): Map<LocalDate, BigDecimal> {
        val data = filterIsInstance(Record.Transaction::class.java).filter { it.type == type}

        if (data.isEmpty()) return emptyMap()

//        val dateRange = LocalDate.now().toYearRange()
//        var currentDate = dateRange.start
//        val yearDates = mutableListOf<LocalDate>()
//
//        while (currentDate.isBefore(dateRange.end)) {
//            yearDates.add(currentDate)
//            currentDate = currentDate.plusMonths(1)
//        }

        // todo add start balance pair for income type
        return data.groupingBy { it.date.toLocalDate().withDayOfMonth(1) }.eachSumBy { it.amount.value }
    }

    fun <T, K> Grouping<T, K>.eachSumBy(selector: (T) -> BigDecimal) = fold(BigDecimal.ZERO) { acc, elem -> acc + selector(elem) }

    override fun onAction(action: Action) {
        TODO("Not yet implemented")
    }
}