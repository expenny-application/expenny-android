package org.expenny.feature.accounts.overview

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.feature.accounts.overview.navigation.AccountOverviewNavArgs
import org.expenny.feature.accounts.overview.view.AccountOverviewContent
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = AccountOverviewNavArgs::class)
@Composable
fun AccountOverviewScreen() {
    val vm: AccountOverviewViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End)

    LaunchedEffect(state.overviewChart) {
        vm.overviewChartModelProducer.tryRunTransaction {
            add(state.overviewChart.model)
        }
    }

    AccountOverviewContent(
        state = state,
        scope = scope,
        chartScrollSpec = chartScrollSpec,
        chartModelProducer = vm.overviewChartModelProducer,
        lazyListState = lazyListState,
        onAction = vm::onAction
    )
}
