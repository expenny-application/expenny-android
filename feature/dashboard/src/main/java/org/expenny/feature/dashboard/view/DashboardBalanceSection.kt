package org.expenny.feature.dashboard.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyRecord
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.RecordUi
import org.expenny.feature.dashboard.model.DashboardBalanceUi

@Composable
internal fun DashboardBalanceSection(
    modifier: Modifier = Modifier,
    balanceData: DashboardBalanceUi,
    onShowMoreRecordsClick: () -> Unit
) {
    ExpennyCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardBalanceHeading(
                balance = balanceData.amount
            )
            DashboardBalanceLastRecord(
                lastRecord = balanceData.lastRecord,
                onShowMoreClick = onShowMoreRecordsClick
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DashboardBalanceHeading(
    modifier: Modifier = Modifier,
    balance: AmountUi?
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.total_balance_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AnimatedContent(
            targetState = balance?.displayValue ?: stringResource(R.string.not_assigned_label),
            transitionSpec = { fadeIn() with fadeOut() },
            label = "DashboardBalanceHeading"
        ) {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun DashboardBalanceLastRecord(
    modifier: Modifier = Modifier,
    lastRecord: RecordUi.Item?,
    onShowMoreClick: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.last_record_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = Modifier.clickable(onClick = onShowMoreClick),
                    text = stringResource(R.string.more_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LastRecordItem(
                modifier = Modifier.fillMaxWidth(),
                lastRecord = lastRecord
            )
        }
    }
}

@Composable
private fun LastRecordItem(
    modifier: Modifier = Modifier,
    lastRecord: RecordUi.Item?
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (lastRecord == null) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = stringResource(R.string.no_records_yet_label)
                )
            }
        } else {
            ExpennyRecord(
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .fillMaxWidth(),
                record = lastRecord,
            )
        }
    }
}

