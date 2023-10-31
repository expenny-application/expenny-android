package org.expenny.core.ui.mapper

import androidx.compose.ui.graphics.Color
import org.expenny.core.model.label.Label
import org.expenny.core.ui.data.ui.LabelUi
import javax.inject.Inject

class LabelMapper @Inject constructor() {

    operator fun invoke(model: Label): LabelUi {
        return LabelUi(
            id = model.id,
            name = model.name,
            color = Color(model.colorArgb)
        )
    }

    operator fun invoke(model: List<Label>): List<LabelUi> {
        return model.map { invoke(it) }
    }
}