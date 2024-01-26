package org.expenny.feature.currencydetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonType

@Composable
internal fun CurrencyDetailsActionButton(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
) {
    ExpennyButton(
        modifier = modifier,
        onClick = onSaveClick,
        attributes = ExpennyFloatingButtonAttributes(
            isExpanded = true,
            label = stringResource(R.string.save_button),
            type = ExpennyFloatingButtonType.Primary,
            size = ExpennyFloatingButtonSize.Large,
            icon = painterResource(R.drawable.ic_check)
        )
    )
}