package org.expenny.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.expenny.core.database.ExpennyDatabase
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpennyDatabase {
        val dbBuilder = Room.databaseBuilder(
            context = context,
            klass = ExpennyDatabase::class.java,
            name = "expenny-database"
        )
        dbBuilder.setQueryCallback(
            queryCallback = object : RoomDatabase.QueryCallback {
                override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                    Timber.i("SQL Query: $sqlQuery, SQL Args: $bindArgs")
                }
            },
            executor = Executors.newSingleThreadExecutor()
        )
        return dbBuilder.build()
    }
}