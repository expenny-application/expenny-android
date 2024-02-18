package org.expenny.main

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import org.expenny.core.ui.utils.ExpennySnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.ui.utils.ExpennyDrawerState

internal class ExpennyState constructor(
    val drawerState: ExpennyDrawerState,
    val snackbarHostState: SnackbarHostState,
    val navHostController: NavHostController,
    val snackbarManager: ExpennySnackbarManager,
    val coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    // Suspends until the snackbar disappears from the screen
                    snackbarHostState.showSnackbar(message)
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }
}

