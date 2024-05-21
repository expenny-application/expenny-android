package org.expenny.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import org.expenny.core.resources.R

@Composable
fun ExpennyAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_image_placeholder),
            tint = MaterialTheme.colorScheme.outlineVariant,
            contentDescription = null
        )
        AsyncImage(
            modifier = modifier,
            model = model,
            contentScale = contentScale,
            contentDescription = null,
        )
    }
}