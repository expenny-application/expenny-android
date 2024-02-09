package org.expenny.feature.recorddetails.view

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import coil.compose.AsyncImage
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyCard

@Composable
internal fun RecordDetailsReceipt(
    modifier: Modifier = Modifier,
    uri: Uri,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val context = LocalContext.current
    val imageName by rememberSaveable(uri) {
        mutableStateOf(DocumentFile.fromSingleUri(context, uri)?.name ?: "")
    }
    ExpennyCard(
        modifier = modifier.height(150.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = uri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            ReceiptDeleteIcon(onClick = onDeleteClick)
            ReceiptImageGradient()
            ReceiptImageCaption(imageName = imageName)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReceiptImageCaption(
    modifier: Modifier = Modifier,
    imageName: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            modifier = Modifier.basicMarquee(),
            text = imageName,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
private fun ReceiptImageGradient(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    startY = 0f,
                    colors = listOf(Color.Transparent, Color.Black)
                )
            )
    )
}

@Composable
private fun BoxScope.ReceiptDeleteIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(2.dp)
            .align(Alignment.TopEnd)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_close),
            tint = Color.White,
            contentDescription = null
        )
    }
}