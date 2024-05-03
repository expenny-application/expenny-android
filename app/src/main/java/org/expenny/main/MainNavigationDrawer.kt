package org.expenny.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.expenny.core.resources.R
import org.expenny.navigation.ExpennyNavGraphs

@Composable
internal fun MainNavigationDrawer(
    currentTab: NavGraphSpec,
    onTabSelect: (NavGraphSpec) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .sizeIn(
                minWidth = 300.dp,
                maxWidth = 300.dp
            )
            .fillMaxHeight(),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 16.dp
        ),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minWidth = 300.dp, maxWidth = 300.dp)
                .padding(PaddingValues(16.dp))
                .verticalScroll(scrollState)
                .animateContentSize()
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NavigationDrawerHeader()
            NavigationDrawerContent(
                modifier = Modifier.weight(1f),
                currentOption = currentTab,
                onTabSelect = { onTabSelect(it.navGraph) }
            )
            NavigationDrawerFooter(
                onOptionSelect = { onTabSelect(it.navGraph) }
            )
        }
    }
}

@Composable
private fun NavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.menu_label),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    currentOption: NavGraphSpec,
    onTabSelect: (DrawerTab) -> Unit,
) {
    val tabs = listOf(
        DrawerTab.Dashboard,
        DrawerTab.Analytics,
        DrawerTab.Budgets,
        DrawerTab.Accounts,
        DrawerTab.Records
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { tab ->
            NavigationDrawerOption(
                isSelected = tab.navGraph == currentOption,
                icon = painterResource(tab.iconResId),
                label = stringResource(tab.labelResId),
                onSelect = {
                    onTabSelect(tab)
                }
            )
        }
    }
}

@Composable
private fun NavigationDrawerFooter(
    modifier: Modifier = Modifier,
    onOptionSelect: (DrawerTab) -> Unit
) {
    val optionType = DrawerTab.Settings

    NavigationDrawerOption(
        modifier = modifier,
        isSelected = false,
        icon = painterResource(optionType.iconResId),
        label = stringResource(optionType.labelResId),
        onSelect = {
            onOptionSelect(optionType)
        },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun NavigationDrawerOption(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    icon: Painter,
    label: String,
    onSelect: () -> Unit,
    trailingContent: @Composable () -> Unit = {},
) {
    val iconColor by rememberUpdatedState(
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
    )
    val labelColor by rememberUpdatedState(
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
    )
    val backgroundColor by rememberUpdatedState(
        if (isSelected) MaterialTheme.colorScheme.primary.copy(0.1f)
        else Color.Transparent
    )
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .height(48.dp)
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = icon,
            tint = iconColor,
            contentDescription = null
        )
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = labelColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        trailingContent()
    }
}

internal enum class DrawerTab(
    val navGraph: NavGraphSpec,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
) {
    Dashboard(ExpennyNavGraphs.dashboard, R.string.dashboard_label, R.drawable.ic_dashboard),
    Analytics(ExpennyNavGraphs.analytics, R.string.analytics_label, R.drawable.ic_analytics),
    Budgets(ExpennyNavGraphs.budgets, R.string.budgets_label, R.drawable.ic_calculator),
    Accounts(ExpennyNavGraphs.accounts, R.string.accounts_label, R.drawable.ic_wallet),
    Records(ExpennyNavGraphs.records, R.string.records_label, R.drawable.ic_records),
    Settings(ExpennyNavGraphs.settings, R.string.settings_label, R.drawable.ic_settings);

    val route = navGraph.startRoute.route

    companion object {
        val routes = entries.map { it.navGraph.route }
        val startRoutes = entries.map { it.navGraph.startRoute.route }
    }
}
