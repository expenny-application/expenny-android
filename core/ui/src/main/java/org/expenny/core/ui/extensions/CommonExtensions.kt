package org.expenny.core.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.expenny.core.ui.foundation.primaryFixed

val isApplicationInDarkMode
    @Composable get() = MaterialTheme.colorScheme.primary != MaterialTheme.colorScheme.primaryFixed