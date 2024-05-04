package org.expenny.feature.institution.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyAsyncImage
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyLoadingContainer
import org.expenny.core.ui.components.ExpennySearchTopBar
import org.expenny.core.ui.components.ExpennyVerticalList
import org.expenny.core.ui.data.InstitutionUi
import org.expenny.feature.institution.contract.InstitutionsListAction
import org.expenny.feature.institution.contract.InstitutionsListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstitutionsListContent(
    state: InstitutionsListState,
    onAction: (InstitutionsListAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpennySearchTopBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.select_institution_label))
                },
                searchQuery = state.searchQuery,
                onQueryChange = { onAction(InstitutionsListAction.OnSearchQueryChange(it)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(InstitutionsListAction.OnBackClick) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        ExpennyLoadingContainer(
            modifier = Modifier.fillMaxSize(),
            isLoading = state.isLoading
        ) {
            InstitutionsList(
                modifier = Modifier.padding(paddingValues),
                institutions = state.institutions,
                onInstitutionClick = { onAction(InstitutionsListAction.OnInstitutionSelect(it)) },
            )
        }
    }
}

@Composable
private fun InstitutionsList(
    modifier: Modifier = Modifier,
    institutions: List<InstitutionUi>,
    onInstitutionClick: (String) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        list = institutions,
        listItemKey = InstitutionUi::id,
        listItem = { item ->
            InstitutionItem(
                modifier = Modifier.fillMaxWidth(),
                institution = item,
                onClick = {
                    onInstitutionClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun InstitutionItem(
    modifier: Modifier = Modifier,
    institution: InstitutionUi,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExpennyAsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small),
                model = institution.logoUrl,
                contentScale = ContentScale.FillBounds
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = institution.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = institution.bic,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}