package org.expenny.core.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


@Composable
fun ExpennyIconBox(
    modifier: Modifier = Modifier,
    icon: Painter,
    color: Color,
    background: Color = color.copy(0.1f),
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(MaterialTheme.shapes.small)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            tint = color,
            contentDescription = null
        )
    }
}
