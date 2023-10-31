package org.expenny.core.ui.data.field

import org.expenny.core.common.utils.StringResource

data class InputField(
    val value: String = "",
    val error: StringResource? = null,
    val enabled: Boolean = true,
    val required: Boolean = true,
)