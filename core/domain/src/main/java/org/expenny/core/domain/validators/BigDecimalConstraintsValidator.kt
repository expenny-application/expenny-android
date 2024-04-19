package org.expenny.core.domain.validators

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import java.math.BigDecimal

class BigDecimalConstraintsValidator(
    private val min: BigDecimal = BigDecimal.valueOf(-999_999_999_999.99),
    private val max: BigDecimal = BigDecimal.valueOf(999_999_999_999.99),
    override val errorMessage: StringResource = StringResource.fromRes(R.string.value_constraints_error)
) : Validator {

    override fun isValid(value: String): Boolean {
        return value.toBigDecimalOrNull()?.let { it in min..max } ?: false
    }
}