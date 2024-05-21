package org.expenny.feature.accounts.type

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.feature.accounts.type.contract.AccountTypeEvent
import org.expenny.feature.accounts.type.navigation.AccountTypeNavigator
import org.expenny.feature.accounts.type.view.AccountTypeContent
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
            is AccountTypeEvent.NavigateToInstitutionsList ->
                navigator.navigateToInstitutionsListScreen()
            is AccountTypeEvent.NavigateBack ->
                navigator.navigateBack()
        }
    }

    AccountTypeContent(
        state = state,
        onAction = vm::onAction
    )
}