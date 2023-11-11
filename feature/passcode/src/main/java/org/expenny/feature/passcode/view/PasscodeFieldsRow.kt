package org.expenny.feature.passcode.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.expenny.core.ui.theme.inputSurface
import org.expenny.feature.passcode.model.PasscodeStatus
import org.expenny.feature.passcode.model.PasscodeType

@Composable
internal fun PasscodeFieldsRow(
    modifier: Modifier = Modifier,
    passcodeType: PasscodeType,
    passcodeStatus: PasscodeStatus,
    passcodeMaxLength: Int,
    passcodeLength: Int,
) {
    val fieldBoxBorderColor = when (passcodeStatus) {
        PasscodeStatus.None -> Color.Transparent
        PasscodeStatus.Invalid -> MaterialTheme.colorScheme.error
        PasscodeStatus.Valid -> {
            when (passcodeType) {
                PasscodeType.Create -> Color.Transparent
                else -> MaterialTheme.colorScheme.primary
            }
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..passcodeMaxLength).forEach { fieldPos ->
            val isFilled by remember(passcodeLength, fieldPos) {
                mutableStateOf(passcodeLength >= fieldPos)
            }
            FieldBox(
                borderColor = fieldBoxBorderColor,
                isFilled = isFilled,
            )
        }
    }
}

@Composable
private fun FieldBox(
    modifier: Modifier = Modifier,
    borderColor: Color,
    isFilled: Boolean,
) {
    val shape = MaterialTheme.shapes.small
    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(height = 56.dp, width = 48.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.inputSurface)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape
                )
        )
        if (isFilled) {
            FieldBoxDot()
        }
    }
}

@Composable
private fun FieldBoxDot(
    modifier: Modifier = Modifier
) {
    val scaleAnimation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            scaleAnimation.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Box(
        modifier = modifier
            .scale(scaleAnimation.value)
            .size(14.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface)
    )
}
