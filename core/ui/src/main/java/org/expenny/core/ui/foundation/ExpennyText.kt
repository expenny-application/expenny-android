package org.expenny.core.ui.foundation

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ExpennyText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 1,
    align: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        color = color,
        textAlign = align,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines
    )
}

@Composable
fun ExpennyText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    maxLines: Int = 1,
    align: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        color = color,
        textAlign = align,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines
    )
}
