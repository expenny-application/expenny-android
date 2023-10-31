package org.expenny.core.common

import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.utils.asRawString
import java.util.UUID

data class SnackbarMessage(val id: Long, val text: String, val type: SnackbarType)

enum class SnackbarType {
    Info, Error
}

class ExpennySnackbarManager(private val resources: Resources) {
    private val _messages: MutableStateFlow<List<SnackbarMessage>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<SnackbarMessage>> get() = _messages.asStateFlow()

    fun showMessage(messageTextRes: StringResource) {
        showMessage(messageTextRes.asRawString(resources))
    }

    fun showMessage(@StringRes messageTextId: Int) {
        showMessage(resources.getText(messageTextId).toString())
    }

    fun showMessage(text: String, type: SnackbarType = SnackbarType.Info) {
        _messages.update { currentMessages ->
            currentMessages + SnackbarMessage(
                id = UUID.randomUUID().mostSignificantBits,
                text = text,
                type = type
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}