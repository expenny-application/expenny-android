package org.expenny.core.ui.data.field

import org.expenny.core.common.utils.StringResource

data class CheckBoxField(
    val value: Boolean = false,
    val error: StringResource? = null,
    val required: Boolean = false,
)