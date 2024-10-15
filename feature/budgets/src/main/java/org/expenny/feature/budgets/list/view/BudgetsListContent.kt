package org.expenny.feature.budgets.list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.ItemActionType
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.components.ExpennyBottomSheet
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyDeleteDialog
import org.expenny.core.ui.components.ExpennyLabel
import org.expenny.core.ui.components.ExpennySegmentedSurfaceTabs
import org.expenny.core.ui.data.PeriodicBudgetUi
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label
import org.expenny.feature.budgets.list.contract.BudgetsListAction
import org.expenny.feature.budgets.list.contract.BudgetsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetsListContent(
    state: BudgetsListState,
    coroutineScope: CoroutineScope,
    actionsSheetState: SheetState,
    periodicBudgetsLazyListState: LazyListState,
    onetimeBudgetsLazyListState: LazyListState,
    drawerState: ExpennyDrawerManager,
    onAction: (BudgetsListAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when (state.dialog) {
        is BudgetsListState.Dialog.DeleteBudgetDialog -> {
            ExpennyDeleteDialog(
                title = stringResource(R.string.delete_budget_question_label),
                body = stringResource(R.string.delete_associated_data_paragraph),
                onDismiss = { onAction(BudgetsListAction.OnDialogDismiss) },
                onConfirm = { onAction(BudgetsListAction.OnDeleteBudgetDialogConfirm) }
            )
        }
        is BudgetsListState.Dialog.BudgetActionsDialog -> {
            BudgetsListActionsSheet(
                scope = coroutineScope,
                actionsSheetState = actionsSheetState,
                actions = state.dialog.actions,
                onActionSelect = { onAction(BudgetsListAction.OnBudgetActionSelect(it)) },
                onDismiss = { onAction(BudgetsListAction.OnDialogDismiss) }
            )
        }
        else -> {}
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetsListToolbar(
                drawerState = drawerState,
                scrollBehavior = scrollBehavior,
                showAddButton = state.showAddButton,
                onAddClick = { onAction(BudgetsListAction.OnOnetimeBudgetCreateClick) },
                onBackClick = { onAction(BudgetsListAction.OnBackClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = when (state.selectedBudgetType) {
                BudgetType.Periodic -> periodicBudgetsLazyListState
                BudgetType.Onetime -> onetimeBudgetsLazyListState
            },
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        ) {
            item {
                ExpennySegmentedSurfaceTabs(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = state.budgetTypes.map { it.label },
                    selectedTabIndex = state.budgetTypes.indexOf(state.selectedBudgetType),
                    onTabSelect = {
                        onAction(BudgetsListAction.OnBudgetTypeChange(state.budgetTypes[it]))
                    }
                )
            }
            when (state.selectedBudgetType) {
                BudgetType.Periodic -> {
                    items(
                        items = state.periodicBudgets,
                        key = { it.id }
                    ) {
                        BudgetItem(
                            modifier = Modifier.fillMaxWidth(),
                            data = it,
                            onClick = { onAction(BudgetsListAction.OnPeriodicBudgetClick(it.id, it.intervalType)) },
                            onLongClick = { onAction(BudgetsListAction.OnPeriodicBudgetLongClick(it.id, it.intervalType)) }
                        )
                    }
                    items(
                        items = state.availableBudgetIntervalTypes
                    ) {
                        PeriodicBudgetIntervalType(
                            data = it,
                            onClick = { onAction(BudgetsListAction.OnPeriodicBudgetCreateClick(it)) }
                        )
                    }
                }
                BudgetType.Onetime -> {

                }
            }
        }
    }
}

@Composable
private fun PeriodicBudgetIntervalType(
    modifier: Modifier = Modifier,
    data: IntervalType,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringArrayResource(R.array.budget_period_type)[data.ordinal],
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            painter = painterResource(R.drawable.ic_add),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = null
        )
    }
}

@Composable
private fun BudgetItem(
    modifier: Modifier = Modifier,
    data: PeriodicBudgetUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val periodType = stringArrayResource(R.array.budget_period_type)[data.intervalType.ordinal]
    val title = stringResource(R.string.periodic_budget_label, periodType)

    ExpennyCard(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = data.limitValue.displayValue,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    progress = { data.progressValue },
                    gapSize = 0.dp,
                    trackColor = MaterialTheme.colorScheme.surface,
                    color = MaterialTheme.colorScheme.primary,
                    drawStopIndicator = null
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = data.spentValue.value.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = data.leftAmount.value.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (data.categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    data.categories.forEach {
                        ExpennyLabel(
                            content = {
                                LabelText(text = it.name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetsListActionsSheet(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    actionsSheetState: SheetState,
    actions: List<ItemActionType>,
    onActionSelect: (ItemActionType) -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyBottomSheet(
        modifier = modifier,
        onDismiss = onDismiss,
        sheetState = actionsSheetState,
        actions = {
            actions.forEach { action ->
                BottomSheetAction(
                    icon = action.icon,
                    text = action.label,
                    onClick = {
                        scope.launch { actionsSheetState.hide() }.invokeOnCompletion {
                            if (!actionsSheetState.isVisible) onActionSelect(action)
                        }
                    }
                )
            }
        }
    )
}