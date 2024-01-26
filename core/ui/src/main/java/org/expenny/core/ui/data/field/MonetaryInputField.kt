package org.expenny.core.ui.data.field

import org.expenny.core.common.utils.StringResource
import java.math.BigDecimal

data class MonetaryInputField(
    val value: BigDecimal = BigDecimal.ZERO.setScale(0),
    val error: StringResource? = null,
    val isEnabled: Boolean = true,
    val isRequired: Boolean = false,
)
