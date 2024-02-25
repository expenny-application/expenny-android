package org.expenny.feature.accountoverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollSpec
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDateRangeFilterButton
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennySingleSelectionDialog
import org.expenny.feature.accountoverview.view.AccountOverviewChartCard
import org.expenny.feature.accountoverview.view.AccountOverviewToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountOverviewContent(
    state: State,
    scope: CoroutineScope,
    chartModelProducer: CartesianChartModelProducer,
    chartScrollSpec: ChartScrollSpec,
    lazyListState: LazyListState,
    onAction: (Action) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when (state.dialog) {
        is State.Dialog.DateRangeSpanDialog -> {
            ExpennySingleSelectionDialog(
                label = stringResource(R.string.date_span_label),
                data = state.dateRangeSpans.map { Pair(it, it.label) },
                selection = state.dateRangeSpanState.dateRangeSpan,
                onSelect = { onAction(Action.Dialog.OnDateRangeSpanSelect(it)) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        else -> {}
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            AccountOverviewToolbar(
                scrollBehavior = scrollBehavior,
                onBackClick = {  },
                onEditClick = {  }
            )
        },
        floatingActionButton = {
            ExpennyDateRangeFilterButton(
                modifier = Modifier.navigationBarsPadding(),
                currentDateRange = state.dateRangeSpanState.dateRangeString,
                onSelectDateRecurrenceClick = { onAction(Action.OnSelectDateRangeSpanClick) },
                onPreviousDateRangeClick = { onAction(Action.OnPreviousDateRangeClick) },
                onNextDateRangeClick = { onAction(Action.OnNextDateRangeClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            AccountOverviewChartCard(
                chartValue = state.totalValue?.displayValue,
                chartModelProducer = chartModelProducer,
                chartScrollSpec = chartScrollSpec,
                trendTypes = state.trendTypes,
                trendType = state.trendType,
                onTrendTypeChange = { onAction(Action.OnTrendTypeChange(it)) }
            )
        }
    }
}