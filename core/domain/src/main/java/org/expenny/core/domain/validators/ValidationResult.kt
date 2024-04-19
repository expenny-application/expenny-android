package org.expenny.core.domain.validators

import org.expenny.core.common.models.StringResource

data class ValidationResult(
    val isValid: Boolean,
    val errorRes: StringResource? = null
)