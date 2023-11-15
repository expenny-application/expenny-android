package org.expenny.core.data.di

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.expenny.core.data.*
import org.expenny.core.data.repository.AccountRepositoryImpl
import org.expenny.core.data.repository.BiometricRepositoryImpl
import org.expenny.core.data.repository.CategoryRepositoryImpl
import org.expenny.core.data.repository.CurrencyRateRepositoryImpl
import org.expenny.core.data.repository.CurrencyRepositoryImpl
import org.expenny.core.data.repository.CurrencyUnitRepositoryImpl
import org.expenny.core.data.repository.FileRepositoryImpl
import org.expenny.core.data.repository.LabelRepositoryImpl
import org.expenny.core.data.repository.LocalRepositoryImpl
import org.expenny.core.data.repository.ProfileRepositoryImpl
import org.expenny.core.data.repository.RecordFileRepositoryImpl
import org.expenny.core.data.repository.RecordLabelRepositoryImpl
import org.expenny.core.data.repository.RecordRepositoryImpl
import org.expenny.core.domain.repository.*

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
    fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindRecordRepository(recordRepositoryImpl: RecordRepositoryImpl): RecordRepository

    @Binds
    fun bindCurrencyRateRepository(currencyRateRepositoryImpl: CurrencyRateRepositoryImpl): CurrencyRateRepository

    @Binds
    fun bindLabelRepository(labelRepositoryImpl: LabelRepositoryImpl): LabelRepository

    @Binds
    fun bindRecordLabelRepository(recordLabelRepositoryImpl: RecordLabelRepositoryImpl): RecordLabelRepository

    @Binds
    fun bindRecordFileRepository(recordFileRepositoryImpl: RecordFileRepositoryImpl): RecordFileRepository

    @Binds
    fun bindFileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository

    @Binds
    fun bindBiometricRepository(biometricRepositoryImpl: BiometricRepositoryImpl): BiometricRepository

    companion object {

        @Provides
        fun provideBiometricManager(@ApplicationContext context: Context): BiometricManager {
            return BiometricManager.from(context)
        }
    }
}