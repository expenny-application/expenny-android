package org.expenny.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toDateRangeString
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview
import org.expenny.core.ui.foundation.primaryContrast
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import java.time.LocalDate

@Composable
fun ExpennyDateRangeFilter(
    modifier: Modifier = Modifier,
    state: IntervalTypeStateReducer.State,
    isVisible: Boolean = true,
    onClick: () -> Unit  = {},
    onNextDateRangeClick: () -> Unit,
    onPreviousDateRangeClick: () -> Unit,
) {
    ExpennyDateRangeFilter(
        modifier = modifier,
        dateRange = state.dateRange,
        bounds = state.bounds,
        isVisible = isVisible,
        onClick = onClick,
        onNextDateRangeClick = onNextDateRangeClick,
        onPreviousDateRangeClick = onPreviousDateRangeClick,
    )
}

@Composable
fun ExpennyDateRangeFilter(
    modifier: Modifier = Modifier,
    dateRange: ClosedRange<LocalDate>,
    bounds: ClosedRange<LocalDate>? = null,
    isVisible: Boolean = true,
    onClick: () -> Unit  = {},
    onNextDateRangeClick: () -> Unit,
    onPreviousDateRangeClick: () -> Unit,
) {
    val showLeftButton by rememberUpdatedState(bounds == null || bounds.start < dateRange.start)
    val showRightButton by rememberUpdatedState(bounds == null || bounds.endInclusive > dateRange.endInclusive)

    AnimatedVisibility(
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
        content = {
            Surface(
                modifier = modifier.semantics {
                    role = Role.Button
                },
                onClick = onClick,
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContrast,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                tonalElevation = 0.dp,
                shadowElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier
                        .sizeIn(minHeight = 56.dp)
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = if (showLeftButton) 0.dp else 24.dp,
                            end = if (showRightButton) 0.dp else 24.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (showLeftButton) {
                        IconButton(onClick = onPreviousDateRangeClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_left),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = dateRange.toDateRangeString(),
                        style = MaterialTheme.typography.titleSmall
                    )
                    if (showRightButton) {
                        IconButton(onClick = onNextDateRangeClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    )
}

private val enterAnimation = fadeIn(
    animationSpec = tween(200)
)+ slideInVertically(
    animationSpec = tween(400),
    initialOffsetY = { it }
)

private val exitAnimation = fadeOut(
    animationSpec = tween(200)
) + slideOutVertically(
    animationSpec = tween(400),
    targetOffsetY = { it }
)

@ExpennyPreview
@Composable
private fun ExpennyDateRangeFilterPreview() {
    ExpennyThemePreview {
        ExpennyDateRangeFilter(
            dateRange = LocalDate.of(2024, 6, 24)
                .rangeTo(LocalDate.of(2024, 7, 1)),
            isVisible = true,
            onClick = {},
            onNextDateRangeClick = {},
            onPreviousDateRangeClick = {}
        )
    }
}