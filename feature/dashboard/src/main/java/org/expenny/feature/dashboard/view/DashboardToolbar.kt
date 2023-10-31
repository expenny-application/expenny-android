package org.expenny.feature.dashboard.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonSize
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar
import org.expenny.core.ui.utils.ExpennyDrawerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    drawerState: ExpennyDrawerState,
    displayCurrency: String? = null,
    onDisplayCurrencyClick: () -> Unit
) {
    ExpennyToolbar(
        scrollBehavior = scrollBehavior,
        title = {
            ExpennyText(text = stringResource(R.string.dashboard_label))
        },
        navigationIcon = {
            drawerState.NavigationDrawerIcon()
        },
        actions = {
            displayCurrency?.let {
                ClickableText(
                    modifier = Modifier.padding(end = 16.dp),
                    text = AnnotatedString(displayCurrency),
                    style = MaterialTheme.typography.titleSmall.copy(
                        MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        onDisplayCurrencyClick()
                    }
                )
            }
        }
    )
}
