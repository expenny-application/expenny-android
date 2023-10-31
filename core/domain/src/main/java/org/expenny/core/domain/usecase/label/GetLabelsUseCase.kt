package org.expenny.core.domain.usecase.label

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.LabelRepository
import org.expenny.core.model.label.Label
import javax.inject.Inject

class GetLabelsUseCase @Inject constructor(
    private val labelRepository: LabelRepository
) {

    operator fun invoke() : Flow<List<Label>> {
        return labelRepository.getLabelsFlow()
    }
}