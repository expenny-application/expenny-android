package org.expenny.core.ui.base

import android.content.res.Resources
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.expenny.core.common.extensions.asRawString
import org.expenny.core.common.models.StringResource
import java.util.UUID

class ExpennySnackbarManager(private val resources: Resources) {
    private val _messages = MutableStateFlow(emptyList<ExpennySnackbarVisuals>())
    val messages get() = _messages.asStateFlow()

    fun showError(error: StringResource) {
        show(error.asRawString(resources), ExpennySnackbarType.Error)
    }

    fun showInfo(info: StringResource) {
        show(info.asRawString(resources), ExpennySnackbarType.Info)
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }

    private fun show(message: String, type: ExpennySnackbarType) {
        _messages.update { currentMessages ->
            currentMessages + ExpennySnackbarVisuals(
                id = UUID.randomUUID().mostSignificantBits,
                type = type,
                message = message
            )
        }
    }
}

data class ExpennySnackbarVisuals(
    val id: Long,
    val type: ExpennySnackbarType,
    override val message: String,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = true
    override val duration: SnackbarDuration = SnackbarDuration.Short
}

enum class ExpennySnackbarType {
    Info, Error
}