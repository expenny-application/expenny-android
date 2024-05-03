package org.expenny.feature.institution.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyLoadingContainer
import org.expenny.core.ui.components.ExpennySearchAppBar
import org.expenny.core.ui.components.ExpennyVerticalList
import org.expenny.core.ui.data.InstitutionCountryUi
import org.expenny.feature.institution.contract.InstitutionCountriesListAction
import org.expenny.feature.institution.contract.InstitutionCountriesListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstitutionCountriesListContent(
    state: InstitutionCountriesListState,
    onAction: (InstitutionCountriesListAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpennySearchAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.select_institution_country_label))
                },
                searchQuery = state.searchQuery,
                onQueryChange = { onAction(InstitutionCountriesListAction.OnSearchQueryChange(it)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(InstitutionCountriesListAction.OnBackClick) }) {
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
            InstitutionCountriesList(
                modifier = Modifier.padding(paddingValues),
                countries = state.countries,
                onCountryClick = { onAction(InstitutionCountriesListAction.OnCountrySelect(it)) },
            )
        }
    }
}

@Composable
private fun InstitutionCountriesList(
    modifier: Modifier = Modifier,
    countries: List<InstitutionCountryUi>,
    onCountryClick: (String) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        list = countries,
        listItemKey = InstitutionCountryUi::code,
        listItem = { item ->
            InstitutionCountryItem(
                modifier = Modifier.fillMaxWidth(),
                country = item,
                onClick = {
                    onCountryClick(item.code)
                }
            )
        }
    )
}

@Composable
private fun InstitutionCountryItem(
    modifier: Modifier = Modifier,
    country: InstitutionCountryUi,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = country.flag)
            Text(
                modifier = Modifier.weight(1f),
                text = country.country,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = country.institutionsCount.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            )
        }
    }
}
