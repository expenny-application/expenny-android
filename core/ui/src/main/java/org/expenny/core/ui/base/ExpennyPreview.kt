package org.expenny.core.ui.base

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark", showBackground = true, backgroundColor = 0x000000, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFFF, uiMode = UI_MODE_NIGHT_NO)
annotation class ExpennyPreview