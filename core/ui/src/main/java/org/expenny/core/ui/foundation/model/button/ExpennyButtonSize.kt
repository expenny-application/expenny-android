package org.expenny.core.ui.foundation.model.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//@Immutable
//enum class ExpennyButtonSize(
//    internal val height: Dp,
//    internal val width: Dp,
//    internal val iconSize: Dp,
//    internal val spacing: Dp,
//    internal val shape: Shape,
//    internal val padding: PaddingValues
//) {
//    Small(
//        height = 32.dp,
//        width = 32.dp,
//        iconSize = 14.dp,
//        spacing = 4.dp,
//        shape = RoundedCornerShape(8.dp),
//        padding = PaddingValues(
//            horizontal = 12.dp,
//            vertical = 8.dp
//        )
//    ),
//    Medium(
//        height = 44.dp,
//        width = 44.dp,
//        iconSize = 16.dp,
//        spacing = 8.dp,
//        shape = RoundedCornerShape(8.dp),
//        padding = PaddingValues(
//            horizontal = 14.dp,
//            vertical = 8.dp
//        )
//    ),
//    Large(
//        height = 56.dp,
//        width = 56.dp,
//        iconSize = 18.dp,
//        spacing = 8.dp,
//        shape = RoundedCornerShape(8.dp),
//        padding = PaddingValues(
//            horizontal = 16.dp,
//            vertical = 10.dp
//        )
//    );
//
//    internal val textStyle: TextStyle
//        @Composable
//        @ReadOnlyComposable
//        get() = when (this) {
//            Small -> MaterialTheme.typography.labelMedium
//            Medium -> MaterialTheme.typography.labelLarge
//            Large -> MaterialTheme.typography.titleMedium
//        }
//}

@Immutable
sealed interface ExpennyButtonSize {
    val height: Dp
    val width: Dp
    val iconSize: Dp
    val spacing: Dp
    val shape: Shape
    val padding: PaddingValues

    @get:Composable
    val textStyle: TextStyle
}

@Immutable
sealed class ExpennyFloatingButtonSize : ExpennyButtonSize {
    internal val expandSizeModifier = 0.85f

    @Immutable
    data object Medium : ExpennyFloatingButtonSize() {
        override val height = 44.dp
        override val width = 44.dp
        override val iconSize = 20.dp
        override val spacing = 8.dp
        override val shape = RoundedCornerShape(8.dp)
        override val padding = PaddingValues(14.dp, 8.dp)
        override val textStyle
            @Composable get() = MaterialTheme.typography.labelLarge
    }

    @Immutable
    data object Large : ExpennyFloatingButtonSize() {
        override val height = 56.dp
        override val width = 56.dp
        override val iconSize = 24.dp
        override val spacing = 8.dp
        override val shape = RoundedCornerShape(8.dp)
        override val padding = PaddingValues(16.dp, 10.dp)
        override val textStyle
            @Composable get() = MaterialTheme.typography.titleMedium
    }
}

@Immutable
sealed class ExpennyFlatButtonSize : ExpennyButtonSize {

    @Immutable
    data object Small : ExpennyFlatButtonSize() {
        override val height = 32.dp
        override val width = 32.dp
        override val iconSize = 14.dp
        override val spacing = 4.dp
        override val shape = RoundedCornerShape(8.dp)
        override val padding = PaddingValues(12.dp, 8.dp)
        override val textStyle
            @Composable get() = MaterialTheme.typography.labelMedium
    }

    @Immutable
    data object Medium : ExpennyFlatButtonSize() {
        override val height = 44.dp
        override val width = 44.dp
        override val iconSize = 16.dp
        override val spacing = 8.dp
        override val shape = RoundedCornerShape(8.dp)
        override val padding = PaddingValues(14.dp, 8.dp)
        override val textStyle
            @Composable get() = MaterialTheme.typography.labelLarge
    }

    @Immutable
    data object Large : ExpennyFlatButtonSize() {
        override val height = 56.dp
        override val width = 56.dp
        override val iconSize = 18.dp
        override val spacing = 8.dp
        override val shape = RoundedCornerShape(8.dp)
        override val padding = PaddingValues(16.dp, 10.dp)
        override val textStyle
            @Composable get() = MaterialTheme.typography.titleMedium
    }
}