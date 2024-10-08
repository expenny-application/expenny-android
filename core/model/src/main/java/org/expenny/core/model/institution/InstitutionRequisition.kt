package org.expenny.core.model.institution

data class InstitutionRequisition(
    val id: String,
    val status: InstitutionRequisitionStatus?,
    val redirectBaseUrl: String,
    val url: String,
    val accounts: List<String>
) {
    val redirectUrl: String
        get() = "${redirectBaseUrl}?ref=${id}"
}
