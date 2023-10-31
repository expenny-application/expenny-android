package org.expenny.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.expenny.main.ExpennyState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import com.ramcosta.composedestinations.scope.resultRecipient
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.foundation.transitions.*
import org.expenny.feature.accountdetails.AccountDetailsScreen
import org.expenny.feature.accountdetails.destinations.AccountDetailsScreenDestination
import org.expenny.feature.accounts.destinations.AccountsListScreenDestination
import org.expenny.feature.categories.destinations.CategoriesListScreenDestination
import org.expenny.feature.currencies.destinations.CurrenciesListScreenDestination
import org.expenny.feature.currencydetails.CurrencyDetailsScreen
import org.expenny.feature.currencydetails.destinations.CurrencyDetailsScreenDestination
import org.expenny.feature.getstarted.GetStartedScreen
import org.expenny.feature.getstarted.destinations.GetStartedScreenDestination
import org.expenny.feature.currencyunits.destinations.CurrencyUnitsListScreenDestination
import org.expenny.feature.dashboard.DashboardScreen
import org.expenny.feature.dashboard.destinations.DashboardScreenDestination
import org.expenny.feature.labels.destinations.LabelsListScreenDestination
import org.expenny.feature.recorddetails.RecordDetailsScreen
import org.expenny.feature.recorddetails.destinations.RecordDetailsScreenDestination

@OptIn(
    ExperimentalAnimationApi::class
)
@Composable
internal fun ExpennyNavigation(
    modifier: Modifier = Modifier,
    expennyState: ExpennyState,
) {
    val engine = rememberNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { defaultEnterNavigationTransition() },
            exitTransition = { defaultExitNavigationTransition() },
            popEnterTransition = { defaultPopEnterNavigationTransition() },
            popExitTransition = { defaultPopExitNavigationTransition() },
        )
    )

    DestinationsNavHost(
        modifier = modifier,
        engine = engine,
        navGraph = ExpennyNavGraphs.root,
        navController = expennyState.navHostController,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
            dependency(navBackStackEntry)
            dependency(expennyState.snackbarManager)
            dependency(expennyState.drawerState)
        }
    ) {
        // Explicit screen destinations to resolve resultRecipient
        composable(GetStartedScreenDestination) {
            GetStartedScreen(
                snackbarManager = expennyState.snackbarManager,
                navigator = currentNavigator(),
                currencyUnitResult = resultRecipient<CurrencyUnitsListScreenDestination, LongNavArg>(),
            )
        }
        composable(AccountDetailsScreenDestination) {
            AccountDetailsScreen(
                snackbarManager = expennyState.snackbarManager,
                navigator = currentNavigator(),
                currencyResult = resultRecipient<CurrenciesListScreenDestination, LongNavArg>(),
            )
        }
        composable(DashboardScreenDestination) {
            DashboardScreen(
                navigator = currentNavigator(),
                currencyResult = resultRecipient<CurrenciesListScreenDestination, LongNavArg>(),
                drawerState = expennyState.drawerState
            )
        }
        composable(CurrencyDetailsScreenDestination) {
            CurrencyDetailsScreen(
                snackbarManager = expennyState.snackbarManager,
                navigator = currentNavigator(),
                currencyUnitResult = resultRecipient<CurrencyUnitsListScreenDestination, LongNavArg>(),
            )
        }
        composable(RecordDetailsScreenDestination) {
            RecordDetailsScreen(
                snackbarManager = expennyState.snackbarManager,
                navigator = currentNavigator(),
                accountResult = resultRecipient<AccountsListScreenDestination, NavArgResult>(),
                categoryResult = resultRecipient<CategoriesListScreenDestination, LongNavArg>(),
                labelsResult = resultRecipient<LabelsListScreenDestination, LongArrayNavArg>(),
            )
        }
    }
}

private fun DestinationScopeWithNoDependencies<*>.currentNavigator(): ExpennyNavigator{
    return ExpennyNavigator(
        navBackStackEntry.destination.navGraph(),
        navController,
    )
}