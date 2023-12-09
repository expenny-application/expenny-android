package org.expenny.core.domain.usecase.profile

import android.graphics.Color
import kotlinx.coroutines.flow.first
import org.expenny.core.common.utils.StringResource.Companion.fromArrayRes
import org.expenny.core.common.utils.StringResourceProvider
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.domain.repository.ProfileRepository
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.profile.ProfileCreate
import org.expenny.core.resources.R
import java.math.BigDecimal
import javax.inject.Inject

class SetupProfileUseCase @Inject constructor(
    private val getProfileSetUp: GetProfileSetUpUseCase,
    private val switchProfileUseCase: SwitchProfileUseCase,
    private val profileRepository: ProfileRepository,
    private val currencyRepository: CurrencyRepository,
    private val provideString: StringResourceProvider,
    private val categoryRepository: CategoryRepository,
    private val currencyUnitRepository: CurrencyUnitRepository
) {

    suspend operator fun invoke(params: Params): Long {
        val currencyCode = currencyUnitRepository.getCurrencyUnit(params.currencyUnitId)!!.code
        val isFreshSetup = getProfileSetUp().first().not()
        val profileId = profileRepository.createProfile(ProfileCreate(params.name, currencyCode))

        // Create primary currency
        currencyRepository.createCurrency(
            CurrencyCreate(
                profileId = profileId,
                code = currencyCode,
                baseToQuoteRate = BigDecimal.ONE,
                isSubscribedToRateUpdates = false
            )
        )

        switchProfileUseCase(SwitchProfileUseCase.Params(profileId))

        if (isFreshSetup) {
            setupDefaultCategories(profileId)
        }

        return profileId
    }

    private suspend fun setupDefaultCategories(profileId: Long) {
        categoryRepository.createCategories(
            listOf(
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 0)), "ic_cart", Color.parseColor("#FFE53935")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 1)), "ic_dinner", Color.parseColor("#FF723CEB")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 2)), "ic_house", Color.parseColor("#FFFFD600")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 3)), "ic_bag", Color.parseColor("#FF42A5F5")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 4)), "ic_family", Color.parseColor("#FFEF6C00")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 5)), "ic_suitcase", Color.parseColor("#FFE040FB")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 6)), "ic_care", Color.parseColor("#FFEC407A")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 7)), "ic_study", Color.parseColor("#FF2962FF")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 8)), "ic_savings", Color.parseColor("#FF00BFA5")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 9)), "ic_bill", Color.parseColor("#FF8D6E63")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 10)), "ic_bus", Color.parseColor("#FFC0CA33")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 11)), "ic_money", Color.parseColor("#FF41C300")),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 12)), "ic_cheque", Color.parseColor("#FF616161")),
            )
        )
    }

    data class Params(
        val name: String,
        val currencyUnitId: Long
    )
}