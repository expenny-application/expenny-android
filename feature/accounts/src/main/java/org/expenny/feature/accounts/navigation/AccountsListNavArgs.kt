package org.expenny.feature.accounts.navigation

import org.expenny.core.ui.data.navargs.NavArgResult

data class AccountsListNavArgs(
    val selection: NavArgResult? = null,
    val excludeIds: LongArray? = null
)