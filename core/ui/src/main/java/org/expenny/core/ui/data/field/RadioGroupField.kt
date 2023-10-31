package org.expenny.core.ui.data.field

import org.expenny.core.common.utils.StringResource

data class RadioGroupField(
    val value: Int = -1,
    val options: List<String> = listOf(),
    val error: StringResource? = null,
    val required: Boolean = false
)