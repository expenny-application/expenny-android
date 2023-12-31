package org.expenny.feature.recorddetails.model

import org.expenny.core.common.utils.StringResource

data class LabelsInputField(
    val value: String = "",
    val error: StringResource? = null,
    val suggestion: String? = null,
    val labels: List<String> = emptyList()
)