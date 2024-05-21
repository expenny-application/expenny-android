package org.expenny.feature.institution.requisition.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.expenny.core.ui.components.ExpennyLoadingContainer
import org.expenny.core.ui.components.ExpennyWebView
import org.expenny.core.ui.components.ExpennyWebViewTopBar
import org.expenny.core.ui.components.rememberExpennyWebViewState
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionAction
import org.expenny.feature.institution.requisition.contract.InstitutionRequisitionState

@Composable
internal fun InstitutionRequisitionContent(
    state: InstitutionRequisitionState,
    onAction: (InstitutionRequisitionAction) -> Unit
) {
    val webViewState = rememberExpennyWebViewState(
        url = state.url.orEmpty(),
        shouldOverrideUrlLoading = { requestUrl ->
            if (requestUrl == state.redirectUrl) {
                onAction(InstitutionRequisitionAction.OnRequisitionGranted)
                true
            } else if (requestUrl.contains("UserCancelledSession")) {
                onAction(InstitutionRequisitionAction.OnRequisitionAborted)
                true
            } else {
                false
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExpennyWebViewTopBar(
                title = webViewState.pageTitle,
                onClose = { onAction(InstitutionRequisitionAction.OnRequisitionAborted) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        ExpennyLoadingContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isLoading = webViewState.isLoading || state.isLoading,
        ) {
            ExpennyWebView(
                modifier = Modifier.fillMaxSize(),
                state = webViewState,
            )
        }
    }
}
