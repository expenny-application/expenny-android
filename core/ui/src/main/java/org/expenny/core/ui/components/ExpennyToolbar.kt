package org.expenny.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    title: @Composable ExpennyToolbarScope.() -> Unit,
    navigationIcon: @Composable ExpennyToolbarScope.() -> Unit = {},
    actions: @Composable ExpennyToolbarScope.() -> Unit = {},
) {
    val scope = remember { ExpennyToolbarScope() }

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = {
            actions(scope)
        },
        navigationIcon = {
            navigationIcon(scope)
        },
        title = {
            title(scope)
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

class ExpennyToolbarScope {

    @Composable
    fun ToolbarTitle(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }

    @Composable
    fun ToolbarLabel(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit
    ) {
        Text(
            modifier = modifier
                .padding(end = 16.dp)
                .noRippleClickable { onClick() },
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }

    @Composable
    fun ToolbarIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Icon(
                painter = painter,
                contentDescription = stringResource(R.string.icon_a11y)
            )
        }
    }
}