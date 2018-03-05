package com.example.rssgrabber.modules

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedDatabase
import com.example.rssgrabber.database.FeedItemDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    val MIGRATION_1: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {}
    }

    @Provides
    @Singleton
    fun provideFeedDatabase(application: Context?): FeedDatabase {
        return Room.databaseBuilder(application!!, FeedDatabase::class.java, "feed.db")
                .addMigrations(MIGRATION_1)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideFeedDao(db: FeedDatabase): FeedDao = db.feedDao()

    @Singleton
    @Provides
    fun provideFeedItemDao(db: FeedDatabase): FeedItemDao = db.feedItemDao()
}