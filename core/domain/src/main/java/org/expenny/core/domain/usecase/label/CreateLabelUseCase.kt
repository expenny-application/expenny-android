package org.expenny.core.domain.usecase.label

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LabelRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.label.LabelCreate
import javax.inject.Inject

class CreateLabelUseCase @Inject constructor(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val labelRepository: LabelRepository
) {

    suspend operator fun invoke(params: Params) {
        val profileId = getCurrentProfile().first()!!.id

        labelRepository.createLabel(
            LabelCreate(
                profileId = profileId,
                name = params.name,
                colorArgb = params.colorArgb
            )
        )
    }

    data class Params(
        val name: String,
        val colorArgb: Int
    )
}