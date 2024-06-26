package org.expenny.feature.passcode.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.expenny.core.ui.foundation.transparent
import org.expenny.feature.passcode.model.PasscodeType
import org.expenny.feature.passcode.model.PasscodeValidationResult

@Composable
internal fun PasscodeInputsRow(
    modifier: Modifier = Modifier,
    passcodeType: PasscodeType,
    passcodeValidationResult: PasscodeValidationResult?,
    passcodeMaxLength: Int,
    passcodeLength: Int,
) {
    val inputBoxBorder = when {
        passcodeValidationResult == PasscodeValidationResult.Invalid ->
            BorderStroke(
                color = MaterialTheme.colorScheme.error,
                width = 2.dp
            )
        passcodeValidationResult == PasscodeValidationResult.Valid
                && passcodeType != PasscodeType.Create ->
            BorderStroke(
                color = MaterialTheme.colorScheme.primary,
                width = 2.dp
            )
        else -> BorderStroke(
            color = MaterialTheme.colorScheme.outlineVariant,
            width = 1.dp
        )
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
            InputBox(
                border = inputBoxBorder,
                isFilled = isFilled,
            )
        }
    }
}

@Composable
private fun InputBox(
    modifier: Modifier = Modifier,
    border: BorderStroke,
    isFilled: Boolean,
) {
    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(height = 56.dp, width = 48.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.transparent)
                .border(
                    border = border,
                    shape = MaterialTheme.shapes.small
                )
        )
        if (isFilled) {
            InputBoxDot()
        }
    }
}

@Composable
private fun InputBoxDot(
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
