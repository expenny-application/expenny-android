package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyLoremIpsum
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyLabel(
    modifier: Modifier = Modifier,
    content: @Composable ExpennyLabelScope.() -> Unit,
    leadingContent: (@Composable ExpennyLabelScope.() -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: (() -> Unit)? = null
) {
    val scope = remember { ExpennyLabelScope() }

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Box(
            modifier = modifier
                .height(28.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(containerColor)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        PaddingValues(
                            start = if (leadingContent == null) 12.dp else 8.dp,
                            end = 12.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 6.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides MaterialTheme.typography.bodySmall
                ) {
                    leadingContent?.invoke(scope)
                    content(scope)
                }
            }
        }
    }
}

@Stable
class ExpennyLabelScope {

    @Composable
    fun LabelText(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier.sizeIn(maxWidth = 130.dp),
            text = text,
            overflow = TextOverflow.Ellipsis
        )
    }

    @Composable
    fun LabelIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
    ) {
        Icon(
            modifier = modifier.size(16.dp),
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y)
        )
    }
}

@ExpennyPreview
@Composable
private fun ExpennyLabelPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyLabel(
            onClick = {},
            content = {
                LabelText(text = ExpennyLoremIpsum(6).text)
            },
            leadingContent = {
                LabelIcon(painter = painterResource(R.drawable.ic_image_placeholder))
            }
        )
    }
}
