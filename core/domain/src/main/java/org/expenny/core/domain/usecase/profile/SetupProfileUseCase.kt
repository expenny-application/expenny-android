package org.expenny.core.domain.usecase.profile

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

        currencyRepository.createCurrency(
            CurrencyCreate(
                profileId = profileId,
                code = currencyCode,
                baseToQuoteRate = BigDecimal.ONE
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
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 0)), "ic_shopping_cart_two_tone", 0xFFF44336),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 1)), "ic_restaurant_two_tone", 0xFF723CEB),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 2)), "ic_house_two_tone", 0xFFFBC02D),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 3)), "ic_shopping_bag_two_tone", 0xFF42A5F5),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 4)), "ic_family_two_tone", 0xFFEF6C00),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 5)), "ic_suitcase_two_tone", 0xFFE040FB),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 6)), "ic_smiling_two_tone", 0xFFEC407A),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 7)), "ic_education_two_tone", 0xFF2962FF),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 8)), "ic_money_safe_two_tone", 0xFF00BFA5),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 9)), "ic_finance_two_tone", 0xFF8D6E63),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 10)), "ic_bus_two_tone", 0xFFC0CA33),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 11)), "ic_salary_two_tone", 0xFF41C300),
                CategoryCreate(profileId, provideString(fromArrayRes(R.array.default_categories, 12)), "ic_transaction_two_tone", 0xFF616161),
            )
        )
    }

    data class Params(
        val name: String,
        val currencyUnitId: Long
    )
}