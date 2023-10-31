package org.expenny.core.domain.validators

import org.expenny.core.resources.R
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.utils.Constants.DEFAULT_FIELD_MIN_LENGTH

class MinimumLengthValidator(
    private val min: Int = DEFAULT_FIELD_MIN_LENGTH,
    override val errorMessage: StringResource = StringResource.fromRes(R.string.required_field_min_length_error, min),
) : Validator {

    override fun isValid(value: String): Boolean {
        return value.length >= min
    }
}