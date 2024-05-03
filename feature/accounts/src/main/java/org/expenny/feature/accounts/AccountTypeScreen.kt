package org.expenny.feature.accounts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.feature.accounts.contract.AccountTypeEvent
import org.expenny.feature.accounts.navigation.AccountTypeNavigator
import org.expenny.feature.accounts.view.AccountTypeContent
import org.expenny.feature.accounts.viewmodel.AccountTypeViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
fun AccountTypeScreen(
    navigator: AccountTypeNavigator,
) {
    val vm: AccountTypeViewModel = hiltViewModel()
    val state by vm.collectAsState()

    vm.collectSideEffect {
        when (it) {
            is AccountTypeEvent.NavigateToCreateAccount ->
                navigator.navigateToCreateAccountScreen()
            is AccountTypeEvent.NavigateToInstitutionCountriesList ->
                navigator.navigateToInstitutionCountriesListScreen()
            is AccountTypeEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    AccountTypeContent(
        state = state,
        onAction = vm::onAction
    )
}