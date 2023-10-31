package org.expenny.core.ui.data.ui

import androidx.compose.ui.graphics.Color
import java.math.BigDecimal

interface ChartEntryUi {
    val value: BigDecimal
    val label: String?
    val color: Color?
}