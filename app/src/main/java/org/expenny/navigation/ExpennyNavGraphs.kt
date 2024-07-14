package org.expenny.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.expenny.feature.accounts.destinations.AccountDetailsScreenDestination
import org.expenny.feature.accounts.destinations.AccountTypeScreenDestination
import org.expenny.feature.accounts.destinations.AccountsListScreenDestination
import org.expenny.feature.analytics.destinations.AnalyticsScreenDestination
import org.expenny.feature.budgets.destinations.BudgetLimitDetailsScreenDestination
import org.expenny.feature.budgets.destinations.BudgetOverviewScreenDestination
import org.expenny.feature.budgets.destinations.BudgetsListScreenDestination
import org.expenny.feature.categories.destinations.CategoriesListScreenDestination
import org.expenny.feature.categories.destinations.CategoryDetailsScreenDestination
import org.expenny.feature.currencies.destinations.CurrenciesListScreenDestination
import org.expenny.feature.currencies.destinations.CurrencyDetailsScreenDestination
import org.expenny.feature.currencyunits.destinations.CurrencyUnitsListScreenDestination
import org.expenny.feature.dashboard.destinations.DashboardScreenDestination
import org.expenny.feature.passcode.destinations.PasscodeScreenDestination
import org.expenny.feature.profilesetup.destinations.ProfileSetupScreenDestination
import org.expenny.feature.records.destinations.RecordDetailsScreenDestination
import org.expenny.feature.records.destinations.RecordsListScreenDestination
import org.expenny.feature.settings.destinations.SettingsScreenDestination
import org.expenny.feature.institution.destinations.InstitutionRequisitionScreenDestination
import org.expenny.feature.institution.destinations.InstitutionsListScreenDestination
import org.expenny.feature.records.destinations.RecordLabelsListScreenDestination
import org.expenny.feature.welcome.destinations.WelcomeScreenDestination

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
            AccountTypeScreenDestination,
            RecordsListScreenDestination,
            RecordDetailsScreenDestination,
            RecordLabelsListScreenDestination,
            CategoriesListScreenDestination,
            CategoryDetailsScreenDestination,
            InstitutionsListScreenDestination,
            InstitutionRequisitionScreenDestination,
            BudgetsListScreenDestination,
            BudgetOverviewScreenDestination,
            BudgetLimitDetailsScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val budgets = object: NavGraphSpec {
        override val route = "budgets"

        override val startRoute = BudgetsListScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            BudgetsListScreenDestination,
            BudgetOverviewScreenDestination,
            BudgetLimitDetailsScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val accounts = object: NavGraphSpec {
        override val route = "accounts"

        override val startRoute = AccountsListScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AccountsListScreenDestination,
            AccountDetailsScreenDestination,
            AccountTypeScreenDestination,
            CurrencyUnitsListScreenDestination,
            CurrenciesListScreenDestination,
            CurrencyDetailsScreenDestination,
            InstitutionsListScreenDestination,
            InstitutionRequisitionScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val settings = object: NavGraphSpec {
        override val route = "settings"

        override val startRoute = SettingsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SettingsScreenDestination,
            CategoriesListScreenDestination,
            CategoryDetailsScreenDestination,
            CurrenciesListScreenDestination,
            CurrencyDetailsScreenDestination,
            CurrencyUnitsListScreenDestination,
            PasscodeScreenDestination,
            ProfileSetupScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val records = object: NavGraphSpec {
        override val route = "records"

        override val startRoute = RecordsListScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            RecordsListScreenDestination,
            RecordDetailsScreenDestination,
            RecordLabelsListScreenDestination,
            AccountsListScreenDestination,
            AccountDetailsScreenDestination,
            CurrencyUnitsListScreenDestination,
            CurrencyDetailsScreenDestination,
            CurrenciesListScreenDestination,
            CategoriesListScreenDestination,
            CategoryDetailsScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val analytics = object: NavGraphSpec {
        override val route = "analytics"

        override val startRoute = AnalyticsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AnalyticsScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val home = object: NavGraphSpec {
        override val route = "home"

        override val startRoute = dashboard

        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val nestedNavGraphs = listOf(
            dashboard,
            records,
            accounts,
            settings,
            analytics,
            budgets
        )
    }

    val auth = object: NavGraphSpec {
        override val route = "security"

        override val startRoute = PasscodeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            PasscodeScreenDestination
        ).routedIn(this).associateBy { it.route }
    }

    val setup = object: NavGraphSpec {
        override val route = "setup"

        override val startRoute = WelcomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            WelcomeScreenDestination,
            ProfileSetupScreenDestination,
            CurrencyUnitsListScreenDestination,
        ).routedIn(this).associateBy { it.route }
    }

    val root = object: NavGraphSpec {
        override val route = "root"

        override val startRoute = setup

        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val nestedNavGraphs = listOf(
            setup,
            home,
            auth
        )
    }

    val navGraphs = listOf(
        root,
        setup,
        home,
        auth,
        dashboard,
        analytics,
        budgets,
        records,
        accounts,
        settings,
    )
}