package org.expenny.main.drawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.expenny.core.resources.R
import org.expenny.main.drawer.DrawerSection.Main
import org.expenny.main.drawer.DrawerSection.Others
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
            verticalArrangement = Arrangement.spacedBy(itemsSpacing)
        ) {
            NavigationDrawerHeader()
            NavigationDrawerContent(
                modifier = Modifier.weight(1f),
                currentOption = currentTab,
                onOptionSelect = { onTabSelect(it.navGraph) }
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
            .padding(horizontal = optionHorizontalPadding),
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
    onOptionSelect: (DrawerTab) -> Unit,
) {
    val sectionColors = NavigationDrawerDefaults.colors()

    val sections = sortedMapOf(
        Main to listOf(
            DrawerTab.Dashboard,
            DrawerTab.Analytics,
            DrawerTab.Budgets,
            DrawerTab.Accounts,
            DrawerTab.Records
        ),
        Others to listOf(DrawerTab.Debts, DrawerTab.Rates),
    )

    val isSelected: (DrawerTab) -> Boolean = { it.navGraph == currentOption }

    var isOthersSectionVisible by rememberSaveable {
        mutableStateOf(currentOption in sections[Others]!!.map { it.navGraph })
    }

    val expandIconAngle: Float by animateFloatAsState(
        targetValue = if (isOthersSectionVisible) 180f else 0f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "ExpandIconAngle"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemsSpacing)
    ) {
        sections.forEach { (section, options) ->
            if (section == Others) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = itemsSpacing),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                NavigationDrawerOption(
                    icon = painterResource(R.drawable.ic_more_horizontal),
                    label = stringResource(R.string.others_label),
                    onSelect = {
                        isOthersSectionVisible = !isOthersSectionVisible
                    },
                    trailingContent = {
                        Icon(
                            modifier = Modifier.rotate(expandIconAngle),
                            painter = painterResource(R.drawable.ic_expand),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null,
                        )
                    }
                )
            }

            if (isOthersSectionVisible || section == Main) {
                NavigationDrawerSection(
                    type = section,
                    options = options,
                    background = sectionColors.sectionColor(section).value,
                ) {
                    NavigationDrawerOption(
                        icon = painterResource(it.iconResId),
                        label = stringResource(it.labelResId),
                        containerColor = sectionColors.containerColor(isSelected(it)).value,
                        iconColor = sectionColors.iconColor(isSelected(it)).value,
                        labelColor = sectionColors.labelColor(isSelected(it), section).value,
                        labelStyle = MaterialTheme.typography.bodyLarge,
                        onSelect = {
                            isOthersSectionVisible = false
                            onOptionSelect(it)
                        }
                    )
                }
            }
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
        icon = painterResource(optionType.iconResId),
        label = stringResource(optionType.labelResId),
        labelStyle = MaterialTheme.typography.bodyLarge,
        onSelect = {
            onOptionSelect(optionType)
        },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                tint = NavigationDrawerDefaults.iconColor,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun NavigationDrawerSection(
    background: Color,
    type: DrawerSection,
    options: List<DrawerTab>,
    optionContent: @Composable (DrawerTab) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .padding(if (type == Others) PaddingValues(8.dp) else PaddingValues(0.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            optionContent(option)
        }
    }
}

@Composable
private fun NavigationDrawerOption(
    modifier: Modifier = Modifier,
    icon: Painter,
    label: String,
    onSelect: () -> Unit,
    containerColor: Color = NavigationDrawerDefaults.containerColor,
    iconColor: Color = NavigationDrawerDefaults.iconColor,
    labelColor: Color = NavigationDrawerDefaults.labelColor,
    labelStyle: TextStyle = NavigationDrawerDefaults.labelStyle,
    trailingContent: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        onClick = onSelect,
        shape = MaterialTheme.shapes.small,
        color = containerColor,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
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
                    style = labelStyle
                )
            }
            trailingContent()
        }
    }
}


object NavigationDrawerDefaults {
    
    val selectedColor: Color @Composable get() = MaterialTheme.colorScheme.primary
    val containerColor: Color @Composable get() = Color.Transparent
    val sectionColor: Color @Composable get() = Color.Transparent
    val iconColor: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    val labelColor: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle: TextStyle @Composable get() = MaterialTheme.typography.bodyLarge

    @Composable
    internal fun sectionPadding(section: DrawerSection): State<PaddingValues> {
        return rememberUpdatedState(
            if (section == Others) PaddingValues(8.dp)
            else PaddingValues(0.dp)
        )
    }

    @Composable
    internal fun labelStyle(selected: Boolean): State<TextStyle> {
        return rememberUpdatedState(
            if (selected) MaterialTheme.typography.titleMedium
            else MaterialTheme.typography.titleMedium
        )
    }

    @Composable
    fun colors() = NavigationDrawerColors(
        containerColor = containerColor,
        sectionColor = sectionColor,
        iconColor = iconColor,
        labelColor = labelColor,
        selectedColor = selectedColor,
    )
}


class NavigationDrawerColors internal constructor(
    val containerColor: Color,
    val sectionColor: Color,
    val iconColor: Color,
    val labelColor: Color,
    val selectedColor: Color,
) {

    @Composable
    internal fun containerColor(selected: Boolean): State<Color> {
        return rememberUpdatedState(
            if (selected) selectedColor.copy(0.1f) else containerColor
        )
    }

    @Composable
    internal fun iconColor(selected: Boolean): State<Color> {
        return rememberUpdatedState(
            if (selected) selectedColor else iconColor
        )
    }

    @Composable
    internal fun labelColor(selected: Boolean, section: DrawerSection): State<Color> {
        return rememberUpdatedState(
            if (selected) selectedColor else if (section == Others) MaterialTheme.colorScheme.onSurfaceVariant else labelColor
        )
    }

    @Composable
    internal fun sectionColor(section: DrawerSection): State<Color> {
        return rememberUpdatedState(
            if (section == Others) MaterialTheme.colorScheme.surface else sectionColor
        )
    }
}

private val optionHorizontalPadding = 12.dp
private val itemsSpacing = 8.dp

internal enum class DrawerSection {
    Main, Others
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
    Debts(ExpennyNavGraphs.debts, R.string.debts_label, R.drawable.ic_debts),
    Rates(ExpennyNavGraphs.rates, R.string.currency_rates_label, R.drawable.ic_rates),
    Settings(ExpennyNavGraphs.settings, R.string.settings_label, R.drawable.ic_settings);

    val route = navGraph.startRoute.route

    companion object {
        val routes = values().map { it.navGraph.route }
        val startRoutes = values().map { it.navGraph.startRoute.route }
    }
}
