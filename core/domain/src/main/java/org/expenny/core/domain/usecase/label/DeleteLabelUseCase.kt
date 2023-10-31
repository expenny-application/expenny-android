package org.expenny.core.domain.usecase.label

import org.expenny.core.domain.repository.LabelRepository
import javax.inject.Inject

class DeleteLabelUseCase @Inject constructor(
    private val labelRepository: LabelRepository
) {

    suspend operator fun invoke(id: Long) {
        labelRepository.deleteLabel(id)
    }
}