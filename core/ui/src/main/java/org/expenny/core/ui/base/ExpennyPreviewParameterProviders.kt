package org.expenny.core.ui.base

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

internal class StringListPreviewParameterProvider : PreviewParameterProvider<List<String>> {
    override val values = sequenceOf(
        ExpennyLoremIpsum(10).text.chunked(30)
    )
}