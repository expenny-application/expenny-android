package org.expenny.core.ui.utils

import android.content.res.Resources
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.utils.asRawString
import java.util.UUID

data class ExpennySnackbarVisuals(
    val id: Long,
    override val message: String,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = true
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

class ExpennySnackbarManager(private val resources: Resources) {
    private val _messages: MutableStateFlow<List<ExpennySnackbarVisuals>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<ExpennySnackbarVisuals>> get() = _messages.asStateFlow()

    fun showMessage(messageRes: StringResource) {
        showMessage(messageRes.asRawString(resources))
    }

    fun showMessage(text: String) {
        _messages.update { currentMessages ->
            currentMessages + ExpennySnackbarVisuals(
                id = UUID.randomUUID().mostSignificantBits,
                message = text
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}