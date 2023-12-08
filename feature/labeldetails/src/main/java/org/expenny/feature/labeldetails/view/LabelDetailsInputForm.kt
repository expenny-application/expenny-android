package org.expenny.feature.labeldetails.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.*
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyLabelColorAlpha
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.foundation.ExpennyInputField
import org.expenny.core.ui.foundation.ExpennyInputFieldHeight
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.feature.labeldetails.State

@SuppressLint("Range")
@Composable
internal fun LabelDetailsInputForm(
    modifier: Modifier = Modifier,
    state: State,
    onNameChange: (String) -> Unit,
    onColorChange: (Int) -> Unit,
    onGenerateRandomColorClick: () -> Unit,
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameInputField(
            modifier = Modifier.fillMaxWidth(),
            state = state.nameInput,
            onValueChange = onNameChange
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val labelColor by remember(state.colorArgb) {
                mutableStateOf(state.colorArgb.let { Color(it.red, it.green, it.blue) })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = modifier
                        .size(ExpennyInputFieldHeight)
                        .clip(RoundedCornerShape(size = 8.dp))
                        .background(labelColor.copy(alpha = ExpennyLabelColorAlpha)),
                    contentAlignment = Alignment.Center
                ) {
                    ExpennyText(
                        text = "ABC",
                        style = MaterialTheme.typography.labelLarge,
                        color = labelColor
                    )
                }
                ColorInputField(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.colorInput,
                    onGenerateRandomColorClick = onGenerateRandomColorClick,
                )
            }
            RgbPicker(
                colorArgb = state.colorArgb,
                onColorArgbChange = {
                    onColorChange(it)
                }
            )
        }
    }
}

@Composable
private fun RgbPicker(
    colorArgb: Int,
    onColorArgbChange: (Int) -> Unit
) {
    var red by remember(colorArgb) { mutableIntStateOf(colorArgb.red) }
    var green by remember(colorArgb) { mutableIntStateOf(colorArgb.green) }
    var blue by remember(colorArgb) { mutableIntStateOf(colorArgb.blue) }

    val newColor by remember(red, green, blue) {
        mutableStateOf(Color(red, green, blue))
    }

    LaunchedEffect(newColor) {
        onColorArgbChange(newColor.toArgb())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ColorSlider(label = "Red", value = red, onValueChange = { red = it })
        ColorSlider(label = "Green", value = green, onValueChange = { green = it })
        ColorSlider(label = "Blue", value = blue, onValueChange = { blue = it })
    }
}

@Composable
private fun ColorSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExpennyText(
            modifier = Modifier.weight(2f),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Slider(
            modifier = Modifier.weight(10f),
            valueRange = 0f..255f,
            value = value.toFloat(),
            steps = 254,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            onValueChange = {
                onValueChange(it.toInt())
            }
        )
        ExpennyText(
            modifier = Modifier.weight(2f),
            text = value.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End
            )
        )
    }
}

@Composable
private fun NameInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = required,
            isEnabled = enabled,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.name_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Composable
private fun ColorInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onGenerateRandomColorClick: () -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = required,
            isEnabled = enabled,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.color_label),
            onValueChange = {},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            trailingContent = {
                IconButton(onClick = onGenerateRandomColorClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = null,
                    )
                }
            }
        )
    }
}