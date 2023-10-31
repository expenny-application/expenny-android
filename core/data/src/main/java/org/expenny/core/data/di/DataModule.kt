package org.expenny.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.expenny.core.data.*
import org.expenny.core.data.repository.AccountRepositoryImpl
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
abstract class DataModule {

    @Binds
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    abstract fun bindCurrencyUnitRepository(currencyUnitRepositoryImpl: CurrencyUnitRepositoryImpl): CurrencyUnitRepository

    @Binds
    abstract fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindRecordRepository(recordRepositoryImpl: RecordRepositoryImpl): RecordRepository

    @Binds
    abstract fun bindCurrencyRateRepository(currencyRateRepositoryImpl: CurrencyRateRepositoryImpl): CurrencyRateRepository

    @Binds
    abstract fun bindLabelRepository(labelRepositoryImpl: LabelRepositoryImpl): LabelRepository

    @Binds
    abstract fun bindRecordLabelRepository(recordLabelRepositoryImpl: RecordLabelRepositoryImpl): RecordLabelRepository

    @Binds
    abstract fun bindRecordFileRepository(recordFileRepositoryImpl: RecordFileRepositoryImpl): RecordFileRepository

    @Binds
    abstract fun bindFileRepository(fileRepositoryImpl: FileRepositoryImpl): FileRepository
}