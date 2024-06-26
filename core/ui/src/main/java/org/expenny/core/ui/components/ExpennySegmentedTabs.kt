package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.magnifier
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.expenny.core.ui.extensions.noRippleClickable

@Composable
fun ExpennySegmentedCardTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTabIndex: Int = 0,
    onTabSelect: (index: Int) -> Unit
) {
    Tabs(
        modifier = Modifier
            .height(36.dp)
            .then(modifier),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tabs = tabs,
        selectedTabIndex = selectedTabIndex,
        onTabSelect = onTabSelect,
    )
}

@Composable
fun ExpennySegmentedSurfaceTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTabIndex: Int = 0,
    onTabSelect: (index: Int) -> Unit
) {
    Tabs(
        modifier = Modifier
            .height(44.dp)
            .then(modifier),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tabs = tabs,
        selectedTabIndex = selectedTabIndex,
        onTabSelect = onTabSelect,
    )
}

@Composable
private fun Tabs(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelect: (index: Int) -> Unit
) {
    val localDensity = LocalDensity.current
    var heightDp by remember { mutableStateOf(0.dp) }

    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
        TabRow(
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .onGloballyPositioned {
                    heightDp = with(localDensity) { it.size.height.toDp() }
                },
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = { tabPositions ->
                TabIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.height(heightDp),
                    text = tab,
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelect(index)
                    }
                )
            }
        }
    }
}

@Composable
private fun TabIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 4.dp)
            .shadow(
                elevation = 0.dp,
                shape = MaterialTheme.shapes.extraSmall
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .zIndex(-1f)
    )
}

@Composable
private fun Tab(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color =
        if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .padding(all = 8.dp)
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.zIndex(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            text = text
        )
    }
}
