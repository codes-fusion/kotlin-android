package com.example.rssgrabber.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(FeedItemObj::class), (FeedObj::class)], version = 1)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedItemDao(): FeedItemDao
    abstract fun feedDao(): FeedDao
}