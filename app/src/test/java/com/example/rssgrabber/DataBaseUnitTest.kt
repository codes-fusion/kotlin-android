package com.example.rssgrabber

import android.arch.persistence.room.Room
import com.example.rssgrabber.database.FeedDatabase
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class DataBaseUnitTest {

    private lateinit var mDatabase: FeedDatabase

    @Before
    @Throws(Exception::class)
    fun initDB() {
        mDatabase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application.applicationContext, FeedDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDB() {
        mDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun fetch() {
        val feeds = mDatabase.feedDao().getAll()
        assertTrue(feeds.isEmpty())

        val feedItems = mDatabase.feedItemDao().getAll()
        assertTrue(feedItems.isEmpty())
    }
}