package org.expenny.feature.welcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyButton
import org.expenny.feature.welcome.contract.WelcomeAction

@Composable
internal fun WelcomeContent(
    onAction: (WelcomeAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(
                top = 48.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(0xFF0756CE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    tint = Color.White,
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(R.string.welcome_header_message),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.welcome_paragraph_message),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        ExpennyButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(WelcomeAction.OnCtaClick) }
        ) {
            ButtonLabel(text = stringResource(R.string.get_started_button))
        }
    }
}