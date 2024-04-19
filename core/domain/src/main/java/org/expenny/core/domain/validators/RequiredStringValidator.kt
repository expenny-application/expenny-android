package org.expenny.core.domain.validators

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R

class RequiredStringValidator(
    override val errorMessage: StringResource = StringResource.fromRes(R.string.required_field_error)
) : Validator, RequiredValidator {

    override fun isValid(value: String): Boolean {
        return value.trim().isNotEmpty()
    }
}