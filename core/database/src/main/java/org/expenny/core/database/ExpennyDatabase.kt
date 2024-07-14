package org.expenny.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.expenny.core.database.dao.AccountDao
import org.expenny.core.database.dao.BudgetDao
import org.expenny.core.database.dao.BudgetGroupDao
import org.expenny.core.database.dao.BudgetGroupBudgetDao
import org.expenny.core.database.dao.CategoryDao
import org.expenny.core.database.dao.CurrencyDao
import org.expenny.core.database.dao.FileDao
import org.expenny.core.database.dao.ProfileDao
import org.expenny.core.database.dao.RecordDao
import org.expenny.core.database.dao.RecordFileDao
import org.expenny.core.database.model.crossref.BudgetGroupBudgetCrossRef
import org.expenny.core.database.model.crossref.RecordFileCrossRef
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.BudgetEntity
import org.expenny.core.database.model.entity.BudgetGroupEntity
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.FileEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.RecordEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity
import org.expenny.core.database.utils.BigDecimalConverter
import org.expenny.core.database.utils.ClosedLocalDateRangeConverter
import org.expenny.core.database.utils.ListConverter
import org.expenny.core.database.utils.LocalDateConverter
import org.expenny.core.database.utils.LocalDateTimeConverter
import org.expenny.core.database.utils.OffsetDateTimeConverter
import org.expenny.core.database.utils.UriConverter

@Database(
    entities = [
        RecordFileCrossRef::class,
        BudgetGroupBudgetCrossRef::class,
        // OnetimeBudgetAccountCrossRef::class,
        // OnetimeBudgetLimitCrossRef::class,
        // PeriodicBudgetLimitCrossRef::class,
        SettlementCurrencyEntity::class,
        // PeriodicBudgetEntity::class,
        // OnetimeBudgetEntity::class,
        BudgetGroupEntity::class,
        BudgetEntity::class,
        ProfileEntity::class,
        CategoryEntity::class,
        AccountEntity::class,
        RecordEntity::class,
        FileEntity::class,
    ],
    version = 7,
)
@TypeConverters(
    BigDecimalConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    OffsetDateTimeConverter::class,
    ClosedLocalDateRangeConverter::class,
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
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetGroupDao(): BudgetGroupDao
    abstract fun budgetGroupBudgetDao(): BudgetGroupBudgetDao
}