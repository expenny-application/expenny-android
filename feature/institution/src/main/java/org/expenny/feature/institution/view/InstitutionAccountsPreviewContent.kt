package org.expenny.feature.institution.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyLoadingContainer
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewAction
import org.expenny.feature.institution.contract.InstitutionAccountsPreviewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstitutionAccountsPreviewContent(
    state: InstitutionAccountsPreviewState,
    onAction: (InstitutionAccountsPreviewAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onAction(InstitutionAccountsPreviewAction.OnCloseClick) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.accounts_preview_label))
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        ExpennyLoadingContainer(
            modifier = Modifier.fillMaxSize(),
            isLoading = state.isLoading
        ) {

        }
    }
}