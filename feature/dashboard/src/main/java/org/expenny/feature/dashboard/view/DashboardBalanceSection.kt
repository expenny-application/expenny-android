package org.expenny.feature.dashboard.view

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyRecord
import org.expenny.core.ui.data.ui.AmountUi
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyDivider
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.feature.dashboard.model.DashboardBalanceUi
import kotlin.math.roundToInt

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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ExpennyText(
            text = stringResource(R.string.total_balance_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AnimatedContent(
            targetState = balance?.displayValue ?: stringResource(R.string.na_label),
            transitionSpec = { fadeIn() with fadeOut() },
            label = "DashboardBalanceHeading"
        ) {
            ExpennyText(
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
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExpennyText(
                    text = stringResource(R.string.last_record_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ExpennyText(
                    modifier = Modifier.clickable(onClick = onShowMoreClick),
                    text = stringResource(R.string.more_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LastRecordItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
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
            val strokeColor = MaterialTheme.colorScheme.outline
            val strokeCornerRadius = 8.dp

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = strokeColor,
                    style = Stroke(
                        width = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = CornerRadius(strokeCornerRadius.toPx())
                )
            }
            ExpennyText(
                align = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(R.string.no_records_yet_label)
            )
        } else {
            ExpennyRecord(
                modifier = Modifier.fillMaxWidth(),
                record = lastRecord,
            )
        }
    }
}

