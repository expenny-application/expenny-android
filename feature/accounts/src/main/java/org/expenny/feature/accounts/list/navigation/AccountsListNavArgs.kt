package org.expenny.feature.accounts.list.navigation

import org.expenny.core.ui.data.navargs.NavArgResult

data class AccountsListNavArgs(
    val selection: NavArgResult? = null,
    val excludeIds: LongArray? = null
)