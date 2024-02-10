package org.expenny.core.domain.usecase.profile

import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.domain.repository.ProfileRepository
import org.expenny.core.domain.usecase.category.CreateDefaultCategoriesUseCase
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.profile.ProfileCreate
import java.math.BigDecimal
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val setCurrentProfile: SetCurrentProfileUseCase,
    private val profileRepository: ProfileRepository,
    private val currencyRepository: CurrencyRepository,
    private val createDefaultCategories: CreateDefaultCategoriesUseCase,
    private val currencyUnitRepository: CurrencyUnitRepository
) {

    suspend operator fun invoke(params: Params): Long {
        val currencyCode = currencyUnitRepository.getCurrencyUnit(params.currencyUnitId)!!.code
        val profileId = profileRepository.createProfile(ProfileCreate(params.name, currencyCode))

        createMainCurrency(profileId, currencyCode)
        setCurrentProfile(SetCurrentProfileUseCase.Params(profileId))
        createDefaultCategories(CreateDefaultCategoriesUseCase.Params(profileId))

        return profileId
    }

    private suspend fun createMainCurrency(profileId: Long, currencyCode: String) {
        currencyRepository.createCurrency(
            CurrencyCreate(
                profileId = profileId,
                code = currencyCode,
                baseToQuoteRate = BigDecimal.ONE,
                isSubscribedToRateUpdates = false
            )
        )
    }

    data class Params(
        val name: String,
        val currencyUnitId: Long
    )
}