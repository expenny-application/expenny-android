package org.expenny.core.ui.data

import org.expenny.core.common.models.StringResource
import java.math.BigDecimal

data class DecimalInputUi(
    val value: BigDecimal = BigDecimal.ZERO.setScale(0),
    val error: StringResource? = null,
    val isEnabled: Boolean = true,
    val isRequired: Boolean = false,
)
