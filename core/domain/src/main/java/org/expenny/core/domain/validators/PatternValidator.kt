package org.expenny.core.domain.validators

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R

class PatternValidator(
    private val pattern: String,
    override val errorMessage: StringResource = StringResource.fromRes(R.string.required_field_pattern),
) : Validator {

    override fun isValid(value: String): Boolean {
        return Regex(pattern).matches(value)
    }
}