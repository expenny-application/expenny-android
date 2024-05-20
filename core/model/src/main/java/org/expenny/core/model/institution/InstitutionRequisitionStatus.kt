package org.expenny.core.model.institution

enum class InstitutionRequisitionStatus {
    Created,
    GivingConsent,
    UndergoingAuthentication,
    Rejected,
    SelectingAccount,
    GrantingAccess,
    Linked,
    Expired;
}