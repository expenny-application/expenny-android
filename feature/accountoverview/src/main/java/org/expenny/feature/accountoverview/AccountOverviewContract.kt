package org.expenny.feature.accountoverview

import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus

data class State(
    val test: String = "",
    val debitCreditChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
    val saldoChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
) {
    val chartEntryModelProducer: ComposedChartEntryModelProducer<ChartEntryModel> =
        debitCreditChartEntryModelProducer + saldoChartEntryModelProducer
}

sealed interface Action {

}

sealed interface Event {

}