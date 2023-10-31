package org.expenny.core.ui.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.common.types.AccountType
import org.expenny.core.resources.R

val AccountType.iconResId: Int
    @DrawableRes
    get() = when (this) {
        AccountType.General -> R.drawable.ic_wallet
        AccountType.Bank -> R.drawable.ic_bank
        AccountType.Card -> R.drawable.ic_credit_card
        AccountType.Savings -> R.drawable.ic_safe
        AccountType.Cash -> R.drawable.ic_cash
    }

val AccountType.labelResId: Int
    @StringRes
    get() = when (this) {
        AccountType.General -> R.string.general_label
        AccountType.Bank -> R.string.bank_label
        AccountType.Card -> R.string.card_label
        AccountType.Savings -> R.string.savings_label
        AccountType.Cash -> R.string.cash_label
    }

val AccountType.label: String
    @Composable
    get() = stringResource(labelResId)

val AccountType.icon: Painter
    @Composable
    get() = painterResource(iconResId)