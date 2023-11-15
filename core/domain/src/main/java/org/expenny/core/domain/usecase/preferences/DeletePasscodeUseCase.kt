package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class DeletePasscodeUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        localRepository.setPasscode(null)
    }
}