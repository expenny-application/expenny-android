package org.expenny.main

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import org.expenny.core.ui.base.ExpennySnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.ui.base.ExpennyDrawerManager

internal class MainState constructor(
    val drawerManager: ExpennyDrawerManager,
    val snackbarManager: ExpennySnackbarManager,
    val snackbarHostState: SnackbarHostState,
    val navHostController: NavHostController,
    val coroutineScope: CoroutineScope,
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
