package org.expenny.feature.analytics

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.ExpennyToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AnalyticsScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpennyToolbar(
                scrollBehavior = scrollBehavior,
                title = {
                    ExpennyText(text = stringResource(R.string.analytics_label))
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->

    }
}