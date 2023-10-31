package org.expenny.feature.welcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import org.expenny.feature.welcome.state.WelcomeUiEvent

@Composable
internal fun WelcomeContent(
    onUiEvent: (WelcomeUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp,
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WelcomeContinueButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onUiEvent(WelcomeUiEvent.OnContinueClick)
            }
        )
    }
}