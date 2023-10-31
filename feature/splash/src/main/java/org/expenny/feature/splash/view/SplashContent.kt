package org.expenny.feature.splash.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R


@Composable
internal fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
//        Text(
//            text = stringResource(R.string.app_name),
//            color = MaterialTheme.colorScheme.onSurface,
//            style = MaterialTheme.typography.titleLarge
//        )
    }
}