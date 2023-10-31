package org.expenny.core.domain.validators

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import java.math.BigDecimal

class RequiredBigDecimalValidator(
    override val errorMessage: StringResource = StringResource.fromRes(R.string.greater_than_zero_error)
) : Validator, RequiredValidator {

    override fun isValid(value: String): Boolean {
        return value.toBigDecimalOrNull()?.let { it > BigDecimal.ZERO } ?: false
    }
}