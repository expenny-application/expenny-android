package org.expenny.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.expenny.core.database.dao.AccountDao
import org.expenny.core.database.dao.CategoryDao
import org.expenny.core.database.dao.CurrencyDao
import org.expenny.core.database.dao.FileDao
import org.expenny.core.database.dao.ProfileDao
import org.expenny.core.database.dao.RecordDao
import org.expenny.core.database.dao.RecordFileDao
import org.expenny.core.database.model.crossref.DisposableBudgetAccountCrossRef
import org.expenny.core.database.model.crossref.PeriodicalBudgetAccountCrossRef
import org.expenny.core.database.model.crossref.PeriodicalBudgetLimitCrossRef
import org.expenny.core.database.model.crossref.RecordFileCrossRef
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.BudgetLimitEntity
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.DisposableBudgetEntity
import org.expenny.core.database.model.entity.FileEntity
import org.expenny.core.database.model.entity.PeriodicalBudgetEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.RecordEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity
import org.expenny.core.database.utils.BigDecimalConverter
import org.expenny.core.database.utils.ListConverter
import org.expenny.core.database.utils.LocalDateConverter
import org.expenny.core.database.utils.LocalDateTimeConverter
import org.expenny.core.database.utils.UriConverter

@Database(
    entities = [
        RecordFileCrossRef::class,
        DisposableBudgetAccountCrossRef::class,
        PeriodicalBudgetAccountCrossRef::class,
        PeriodicalBudgetLimitCrossRef::class,
        SettlementCurrencyEntity::class,
        PeriodicalBudgetEntity::class,
        DisposableBudgetEntity::class,
        BudgetLimitEntity::class,
        ProfileEntity::class,
        CategoryEntity::class,
        AccountEntity::class,
        RecordEntity::class,
        FileEntity::class,
    ],
    version = 5,
)
@TypeConverters(
    BigDecimalConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    UriConverter::class,
    ListConverter::class
)
abstract class ExpennyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun accountDao(): AccountDao
    abstract fun recordDao(): RecordDao
    abstract fun fileDao(): FileDao
    abstract fun categoryDao(): CategoryDao
    abstract fun profileDao(): ProfileDao
    abstract fun recordFileDao(): RecordFileDao
}