package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyLoremIpsum
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview

@Composable
fun ExpennyMessage(
    modifier: Modifier = Modifier,
    content: @Composable ExpennyMessageScope.() -> Unit,
) {
    val scope = remember { ExpennyMessageScope() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onTertiaryContainer,
                LocalTextStyle provides MaterialTheme.typography.bodyMedium
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Top),
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null
                )
                content(scope)
            }
        }
    }
}

@Stable
class ExpennyMessageScope {

    @Composable
    fun MessageText(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = text
        )
    }
}

@ExpennyPreview
@Composable
private fun ExpennyLabelPreview() {
    ExpennyThemePreview {
        ExpennyMessage {
            MessageText(text = ExpennyLoremIpsum(20).text)
        }
    }
}