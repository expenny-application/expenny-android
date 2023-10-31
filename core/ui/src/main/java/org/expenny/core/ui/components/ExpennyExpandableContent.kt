package org.expenny.core.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun ExpennyExpandableContent(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    content: @Composable () -> Unit
) {
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(100)
        ) + fadeIn(
            animationSpec = tween(100)
        )
    }

    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(100)
        ) + fadeOut(
            animationSpec = tween(100)
        )
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        content()
    }
}
