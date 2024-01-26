package org.expenny.core.ui.foundation.model.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
sealed interface ExpennyButtonType {
    private val disabledContainerAlpha get() = 0.12f
    private val disabledContentAlpha get() = 0.38f

    @get:Composable
    val containerColor: Color

    @get:Composable
    val contentColor: Color

    val disabledContainerColor: Color
        @Composable get() = containerColor.copy(disabledContainerAlpha)

    val disabledContentColor: Color
        @Composable get() = containerColor.copy(disabledContentAlpha)

    @get:Composable
    val rippleColor: Color
}

@Immutable
sealed class ExpennyFlatButtonType : ExpennyButtonType {

    @Immutable
    data object Primary : ExpennyFlatButtonType() {

        override val containerColor: Color
            @Composable get() = MaterialTheme.colorScheme.primary

        override val contentColor: Color
            @Composable get() = MaterialTheme.colorScheme.onPrimary

        override val rippleColor: Color
            @Composable get() = MaterialTheme.colorScheme.surface
    }

    @Immutable
    data object Secondary : ExpennyFlatButtonType() {

        override val containerColor: Color
            @Composable get() = MaterialTheme.colorScheme.secondaryContainer

        override val contentColor: Color
            @Composable get() = MaterialTheme.colorScheme.onSecondaryContainer

        override val rippleColor: Color
            @Composable get() = MaterialTheme.colorScheme.secondary
    }

    @Immutable
    data object Tertiary : ExpennyFlatButtonType() {

        override val containerColor: Color
            @Composable get() = Color.Transparent

        override val contentColor: Color
            @Composable get() = MaterialTheme.colorScheme.primary

        override val rippleColor: Color
            @Composable get() = MaterialTheme.colorScheme.primary
    }
}

@Immutable
sealed class ExpennyFloatingButtonType : ExpennyButtonType {

    @Immutable
    data object Primary : ExpennyFloatingButtonType() {

        override val containerColor: Color
            @Composable get() = MaterialTheme.colorScheme.primary

        override val contentColor: Color
            @Composable get() = MaterialTheme.colorScheme.onPrimary

        override val rippleColor: Color
            @Composable get() = MaterialTheme.colorScheme.surface
    }

    @Immutable
    data object Secondary : ExpennyFloatingButtonType() {

        override val containerColor: Color
            @Composable get() = MaterialTheme.colorScheme.secondaryContainer

        override val contentColor: Color
            @Composable get() = MaterialTheme.colorScheme.onSecondaryContainer

        override val rippleColor: Color
            @Composable get() = MaterialTheme.colorScheme.secondary
    }
}