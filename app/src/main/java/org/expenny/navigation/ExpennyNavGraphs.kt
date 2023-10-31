package org.expenny.navigation

import org.expenny.feature.splash.destinations.SplashScreenDestination
import org.expenny.feature.welcome.destinations.WelcomeScreenDestination
import org.expenny.feature.getstarted.destinations.GetStartedScreenDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.expenny.feature.accountdetails.destinations.AccountDetailsScreenDestination
import org.expenny.feature.accountoverview.destinations.AccountOverviewScreenDestination
import org.expenny.feature.accounts.destinations.AccountsListScreenDestination
import org.expenny.feature.analytics.destinations.AnalyticsScreenDestination
import org.expenny.feature.categories.destinations.CategoriesListScreenDestination
import org.expenny.feature.currencies.destinations.CurrenciesListScreenDestination
import org.expenny.feature.currencydetails.destinations.CurrencyDetailsScreenDestination
import org.expenny.feature.currencyunits.destinations.CurrencyUnitsListScreenDestination
import org.expenny.feature.dashboard.destinations.DashboardScreenDestination
import org.expenny.feature.labeldetails.destinations.LabelDetailsScreenDestination
import org.expenny.feature.labels.destinations.LabelsListScreenDestination
import org.expenny.feature.recorddetails.destinations.RecordDetailsScreenDestination
import org.expenny.feature.records.destinations.RecordsListScreenDestination
import org.expenny.feature.settings.destinations.SettingsScreenDestination

object ExpennyNavGraphs {

    val dashboard = object: NavGraphSpec {
        override val route = "dashboard"

        override val startRoute = DashboardScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            DashboardScreenDestination,
            CurrencyUnitsListScreenDestination,
            CurrenciesListScreenDestination,
            CurrencyDetailsScreenDestination,
            AccountsListScreenDestination,
            AccountDetailsScreenDestination,
            AccountOverviewScreenDestination,
            RecordsListScreenDestination,
            RecordDetailsScreenDestination,
            CategoriesListScreenDestination,
            LabelsListScreenDestination,
            LabelDetailsScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val budgets = object: NavGraphSpec {
        override val route = "budgets"

        override val startRoute = AnalyticsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(

        ).routedIn(this).associateBy { it.route }
    }

    val accounts = object: NavGraphSpec {
        override val route = "accounts"

        override val startRoute = AccountsListScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AccountsListScreenDestination,
            AccountDetailsScreenDestination,
            CurrencyUnitsListScreenDestination,
            CurrenciesListScreenDestination,
            CurrencyDetailsScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val debts = object: NavGraphSpec {
        override val route = "debts"

        override val startRoute = AnalyticsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(

        ).routedIn(this).associateBy { it.route }
    }

    val rates = object: NavGraphSpec {
        override val route = "rates"

        override val startRoute = AnalyticsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(

        ).routedIn(this).associateBy { it.route }
    }

    val settings = object: NavGraphSpec {
        override val route = "settings"

        override val startRoute = SettingsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SettingsScreenDestination,
            CurrenciesListScreenDestination,
            CurrencyDetailsScreenDestination,
            CurrencyUnitsListScreenDestination,
            LabelsListScreenDestination,
            LabelDetailsScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val records = object: NavGraphSpec {
        override val route = "records"

        override val startRoute = RecordsListScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            RecordsListScreenDestination,
            RecordDetailsScreenDestination,
            AccountsListScreenDestination,
            AccountDetailsScreenDestination,
            CurrencyUnitsListScreenDestination,
            CurrencyDetailsScreenDestination,
            CurrenciesListScreenDestination,
            CategoriesListScreenDestination,
            LabelsListScreenDestination,
            LabelDetailsScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val analytics = object: NavGraphSpec {
        override val route = "analytics"

        override val startRoute = AnalyticsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AnalyticsScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val tabs = object: NavGraphSpec {
        override val route = "tabs"

        override val startRoute = dashboard

        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val nestedNavGraphs = listOf(
            dashboard,
            records,
            accounts,
            settings
        )
    }

    val setup = object: NavGraphSpec {
        override val route = "setup"

        override val startRoute = WelcomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            WelcomeScreenDestination,
            GetStartedScreenDestination,
            CurrencyUnitsListScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val root = object: NavGraphSpec {
        override val route = "root"

        override val startRoute = SplashScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SplashScreenDestination,
        ).routedIn(this).associateBy { it.route }

        override val nestedNavGraphs = listOf(
            setup,
            tabs
        )
    }

    val navGraphs = listOf(root, setup, dashboard, analytics, records, budgets, accounts, debts, settings, rates)
}