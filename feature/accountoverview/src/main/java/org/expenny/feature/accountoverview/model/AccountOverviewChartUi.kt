package org.expenny.feature.accountoverview.model

import androidx.compose.runtime.Stable
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import org.expenny.core.model.common.DatedAmount
import java.time.LocalDate

@Stable
data class AccountOverviewChartUi(
    val data: List<DatedAmount> = emptyList(),
) {
    private val axesValues = data.associate {
        it.date.toEpochDay().toFloat() to it.amount
    }

    val model: LineCartesianLayerModel.Partial
        get() = LineCartesianLayerModel.partial {
            series(axesValues.keys, axesValues.values)
        }
}
