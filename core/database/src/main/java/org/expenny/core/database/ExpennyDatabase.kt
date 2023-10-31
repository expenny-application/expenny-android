package org.expenny.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.expenny.core.database.dao.*
import org.expenny.core.database.model.crossref.*
import org.expenny.core.database.model.entity.*
import org.expenny.core.database.utils.BigDecimalConverter
import org.expenny.core.database.utils.LocalDateConverter
import org.expenny.core.database.utils.LocalDateTimeConverter
import org.expenny.core.database.utils.UriConverter

@Database(
    entities = [
        RecordLabelCrossRef::class,
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
        LabelEntity::class,
        FileEntity::class,
    ],
    version = 3,
)
@TypeConverters(
    BigDecimalConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    UriConverter::class,
)
abstract class ExpennyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun accountDao(): AccountDao
    abstract fun recordDao(): RecordDao
    abstract fun fileDao(): FileDao
    abstract fun labelDao(): LabelDao
    abstract fun categoryDao(): CategoryDao
    abstract fun profileDao(): ProfileDao
    abstract fun recordLabelDao(): RecordLabelDao
    abstract fun recordFileDao(): RecordFileDao
}