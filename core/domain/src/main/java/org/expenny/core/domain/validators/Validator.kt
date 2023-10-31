package org.expenny.core.domain.validators

import org.expenny.core.common.utils.StringResource

interface Validator {
    val errorMessage: StringResource

    fun isValid(value: String): Boolean

    fun validate(value: String?): ValidationResult {
        return if (isValid(value.orEmpty())) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(isValid = false, errorRes = errorMessage)
        }
    }
}