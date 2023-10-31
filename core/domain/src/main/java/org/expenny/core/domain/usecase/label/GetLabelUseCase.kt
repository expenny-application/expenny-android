package org.expenny.core.domain.usecase.label

import org.expenny.core.domain.repository.LabelRepository
import org.expenny.core.model.label.Label
import javax.inject.Inject

class GetLabelUseCase @Inject constructor(
    private val labelRepository: LabelRepository
) {

    suspend operator fun invoke(params: Params) : Label? {
        return labelRepository.getLabel(params.id)
    }

    data class Params(
        val id: Long
    )
}