package org.expenny.core.data.di

import android.app.AlarmManager
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.expenny.core.data.repository.AccountRepositoryImpl
import org.expenny.core.data.repository.BiometricRepositoryImpl
import org.expenny.core.data.repository.CategoryRepositoryImpl
import org.expenny.core.data.repository.CurrencyRateRepositoryImpl
import org.expenny.core.data.repository.CurrencyRepositoryImpl
import org.expenny.core.data.repository.CurrencyUnitRepositoryImpl
import org.expenny.core.data.repository.FileRepositoryImpl
import org.expenny.core.data.repository.ProfileRepositoryImpl
import org.expenny.core.data.repository.RecordFileRepositoryImpl
import org.expenny.core.data.repository.RecordRepositoryImpl
import org.expenny.core.data.repository.AlarmRepositoryImpl
import org.expenny.core.data.repository.BudgetGroupRepositoryImpl
import org.expenny.core.data.repository.BudgetRepositoryImpl
import org.expenny.core.data.repository.InstitutionAccountRepositoryImpl
import org.expenny.core.data.repository.InstitutionCountryRepositoryImpl
import org.expenny.core.data.repository.InstitutionRepositoryImpl
import org.expenny.core.data.repository.InstitutionRequisitionRepositoryImpl
import org.expenny.core.data.repository.WorkRepositoryImpl
import org.expenny.core.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    fun bindCurrencyUnitRepository(currencyUnitRepositoryImpl: CurrencyUnitRepositoryImpl): CurrencyUnitRepository

    @Binds
    fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindRecordRepository(recordRepositoryImpl: RecordRepositoryImpl): RecordRepository

    @Binds
    fun bindCurrencyRateRepository(currencyRateRepositoryImpl: CurrencyRateRepositoryImpl): CurrencyRateRepository

    @Binds
    fun bindRecordFileRepository(recordFileRepositoryImpl: RecordFileRepositoryImpl): RecordFileRepository

    @Binds
    fun bindFileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository

    @Binds
    fun bindBiometricRepository(biometricRepositoryImpl: BiometricRepositoryImpl): BiometricRepository

    @Binds
    fun bindWorkRepository(workRepositoryImpl: WorkRepositoryImpl): WorkRepository

    @Binds
    fun bindReminderRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository

    @Binds
    fun bindInstitutionRepository(institutionRepositoryImpl: InstitutionRepositoryImpl): InstitutionRepository

    @Binds
    fun bindInstitutionCountryRepository(institutionCountryRepositoryImpl: InstitutionCountryRepositoryImpl): InstitutionCountryRepository

    @Binds
    fun bindInstitutionRequisitionRepository(institutionRequisitionRepositoryImpl: InstitutionRequisitionRepositoryImpl): InstitutionRequisitionRepository

    @Binds
    fun bindInstitutionAccountRepository(institutionAccountRepositoryImpl: InstitutionAccountRepositoryImpl): InstitutionAccountRepository

    @Binds
    fun bindBudgetGroupRepository(budgetGroupRepositoryImpl: BudgetGroupRepositoryImpl): BudgetGroupRepository

    @Binds
    fun bindBudgetLimitRepository(budgetLimitRepositoryImpl: BudgetRepositoryImpl): BudgetRepository

    companion object {

        @Provides
        fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager {
            return BiometricManager.from(context)
        }

        @Provides
        @Singleton
        internal fun provideWorkManager(@ApplicationContext applicationContext: Context): WorkManager {
            return WorkManager.getInstance(applicationContext)
        }

        @Provides
        @Singleton
        internal fun provideAlarmManager(@ApplicationContext applicationContext: Context): AlarmManager {
            return applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }
}