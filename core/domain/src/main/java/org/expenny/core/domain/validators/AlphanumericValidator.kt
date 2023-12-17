package org.expenny.core.domain.validators

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R

class AlphanumericValidator(
    override val errorMessage: StringResource = StringResource.fromRes(R.string.alphanumeric_error)
) : Validator, RequiredValidator {

    override fun isValid(value: String): Boolean {
        return value.trim().all { it.isLetterOrDigit() || it.isWhitespace() }
    }
}