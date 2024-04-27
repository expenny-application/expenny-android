package org.expenny.core.ui.data

import org.expenny.core.common.models.StringResource

data class CheckboxInputUi(
    val value: Boolean = false,
    val error: StringResource? = null,
    val isRequired: Boolean = false,
)