package org.expenny.core.ui.foundation.model.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
sealed class ExpennyButtonAttributes {
    abstract val type: ExpennyButtonType
    abstract val size: ExpennyButtonSize
    abstract val label: String?
    abstract val icon: Painter?
    abstract val isEnabled: Boolean
}

@Immutable
class ExpennyFlatButtonAttributes(
    override val type: ExpennyFlatButtonType,
    override val size: ExpennyFlatButtonSize,
    override val label: String?,
    override val icon: Painter? = null,
    override val isEnabled: Boolean = true,
) : ExpennyButtonAttributes()

@Immutable
class ExpennyFloatingButtonAttributes(
    override val type: ExpennyFloatingButtonType,
    override val size: ExpennyFloatingButtonSize,
    override val label: String? = null,
    override val icon: Painter?,
    override val isEnabled: Boolean = true,
    val isExpanded: Boolean = false
) : ExpennyButtonAttributes()
