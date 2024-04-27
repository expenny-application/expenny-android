package org.expenny.core.ui.data

import org.expenny.core.common.models.StringResource

data class InputUi(
    val value: String = "",
    val error: StringResource? = null,
    val isEnabled: Boolean = true,
    val isRequired: Boolean = false,
)