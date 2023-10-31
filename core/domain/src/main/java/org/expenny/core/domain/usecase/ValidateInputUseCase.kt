package org.expenny.core.domain.usecase

import org.expenny.core.domain.validators.RequiredValidator
import org.expenny.core.domain.validators.ValidationResult
import org.expenny.core.domain.validators.Validator
import javax.inject.Inject

class ValidateInputUseCase @Inject constructor() {

    operator fun invoke(input: String, validators: List<Validator>): ValidationResult {
        val validResult = ValidationResult(isValid = true)
        val isRequired = validators.firstOrNull { it is RequiredValidator }?.let { true } ?: false

        return if (isRequired || input.isNotEmpty() && validators.isNotEmpty()) {
            validators.asSequence()
                .map { it.validate(input) }
                .firstOrNull { !it.isValid }
                ?: validResult
        } else {
            validResult
        }
    }
}