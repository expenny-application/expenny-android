package org.expenny.core.domain.validators

import android.graphics.Color
import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R

class ColorHexValidator(
    override val errorMessage: StringResource = StringResource.fromRes(R.string.invalid_color_value_error),
)  : Validator {

    override fun isValid(value: String): Boolean {
        return try {
            Color.parseColor(value)
            true
        } catch (e: java.lang.IllegalArgumentException) {
            false
        }
    }
}