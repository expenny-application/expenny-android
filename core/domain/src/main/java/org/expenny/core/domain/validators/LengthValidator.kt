package org.expenny.core.domain.validators

import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.utils.Constants
import org.expenny.core.resources.R

class LengthValidator(
    private val min: Int = Constants.DEFAULT_FIELD_MIN_LENGTH,
    private val max: Int = Constants.DEFAULT_FIELD_MAX_LENGTH,
    override val errorMessage: StringResource = StringResource.fromRes(R.string.required_field_length_error, min, max)
) : Validator {

    override fun isValid(value: String): Boolean {
        return value.length in min..max
    }
}