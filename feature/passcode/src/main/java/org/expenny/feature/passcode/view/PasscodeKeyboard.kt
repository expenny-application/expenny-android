package org.expenny.feature.passcode.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.theme.ExpennyTheme

@Composable
internal fun PasscodeKeyboard(
    modifier: Modifier = Modifier,
    isBackspaceEnabled: Boolean,
    isBiometricEnabled: Boolean,
    onDigitClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    onFingerprintClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val rows = 4
    val columns = 3

    LazyVerticalGrid(
        modifier = modifier.wrapContentSize(),
        columns = GridCells.Fixed(3),

        userScrollEnabled = false,
    ) {
        (1..(rows * columns)).forEach { index ->
            item {
                if (index <= 9 || index == 11) {
                    val digit = if (index <= 9) index else 0
                    DigitButton(
                        digit = digit,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDigitClick(it)
                        },
                    )
                } else {
                    if (index == 10) {
                        BiometricButton(
                            isEnabled = isBiometricEnabled,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onFingerprintClick()
                            }
                        )
                    } else if (index == 12) {
                        BackspaceButton(
                            isEnabled = isBackspaceEnabled,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onBackspaceClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BiometricButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val iconColor =
        if (isEnabled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant

    KeyboardButton(
        modifier = modifier,
        isEnabled = isEnabled,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_biometric),
                tint = iconColor,
                contentDescription = null
            )
        },
        onClick = onClick
    )
}

@Composable
private fun BackspaceButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val iconColor =
        if (isEnabled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant

    KeyboardButton(
        modifier = modifier,
        isEnabled = isEnabled,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_backspace),
                tint = iconColor,
                contentDescription = null
            )
        },
        onClick = onClick
    )
}

@Composable
private fun DigitButton(
    modifier: Modifier = Modifier,
    digit: Int,
    onClick: (Int) -> Unit
) {
    KeyboardButton(
        modifier = modifier,
        isEnabled = true,
        onClick = { onClick(digit) },
        content = {
            Text(
                text = digit.toString(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    )
}

@Composable
private fun KeyboardButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .padding(12.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        if (isEnabled) onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PasscodeKeyboardPreview() {
    ExpennyTheme {
        Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            PasscodeKeyboard(
                modifier = Modifier.wrapContentSize(),
                isBackspaceEnabled = true,
                isBiometricEnabled = true,
                onDigitClick = {},
                onBackspaceClick = {},
                onFingerprintClick = {}
            )
        }
    }
}