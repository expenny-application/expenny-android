package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyLabel(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Box(
            modifier = modifier
                .heightIn(min = 28.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 6.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSecondaryContainer,
                    LocalTextStyle provides MaterialTheme.typography.labelMedium
                ) {
                    if (leadingIcon != null) {
                        Box(modifier = Modifier.size(16.dp)) {
                            leadingIcon()
                        }
                    }
                    label()
                    if (trailingIcon != null) {
                        Box(modifier = Modifier.size(16.dp)) {
                            trailingIcon()
                        }
                    }
                }
            }
        }
    }
}
