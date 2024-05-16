package org.expenny.core.ui.extensions

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable

internal val NavigationBarBottomPadding
    @Composable get() = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()