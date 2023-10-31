package org.expenny.core.domain.usecase.label

import org.expenny.core.domain.repository.LabelRepository
import org.expenny.core.model.label.LabelUpdate
import javax.inject.Inject

class UpdateLabelUseCase @Inject constructor(
    private val labelRepository: LabelRepository
) {

    suspend operator fun invoke(params: Params) {
        labelRepository.updateLabel(
            LabelUpdate(
                id = params.id,
                name = params.name,
                colorArgb = params.colorArgb
            )
        )
    }

    data class Params(
        val id: Long,
        val name: String,
        val colorArgb: Int
    )
}