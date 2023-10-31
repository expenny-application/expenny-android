package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.extensions.noRippleClickable


@Composable
fun ExpennySegmentedTabRow(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    selectedTabIndex: Int = 0,
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
            contentColor = contentColorFor(containerColor),
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
                    textStyle = textStyle,
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
    textStyle: TextStyle,
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
        ExpennyText(
            modifier = Modifier.zIndex(1f),
            text = text,
            style = textStyle,
            color = color
        )
    }
}
