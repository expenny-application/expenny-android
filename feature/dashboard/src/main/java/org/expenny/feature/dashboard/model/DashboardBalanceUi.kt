package org.expenny.feature.dashboard.model

import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.RecordUi

data class DashboardBalanceUi(
    val lastRecord: RecordUi.Item? = null,
    val amount: AmountUi? = null,
)