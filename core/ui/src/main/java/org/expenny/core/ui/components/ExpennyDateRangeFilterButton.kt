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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R

@Composable
fun ExpennyDateRangeFilterButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    currentDateRange: String,
    onSelectDateRecurrenceClick: () -> Unit,
    onNextDateRangeClick: () -> Unit,
    onPreviousDateRangeClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = enterAnimation,
        exit = exitAnimation,
        content = {
            Surface(
                modifier = modifier,
                onClick = onSelectDateRecurrenceClick,
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onPreviousDateRangeClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_left),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = currentDateRange,
                        style = MaterialTheme.typography.titleSmall
                    )
                    IconButton(onClick = onNextDateRangeClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null
                        )
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