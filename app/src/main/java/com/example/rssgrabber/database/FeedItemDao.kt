package com.example.rssgrabber.database

import android.arch.persistence.room.*

@Dao
interface FeedItemDao {
    @Query("SELECT * FROM feed_items")
    fun getAll(): List<FeedItemObj>

    @Query("SELECT * FROM feed_items WHERE id IN (:id)")
    fun loadAllByIds(id: LongArray): List<FeedItemObj>

    @Query("SELECT * FROM feed_items WHERE feed_id = :feed_id ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun findAllByFeed(feed_id: Long, limit: Int, offset: Int): List<FeedItemObj>

    @Query("SELECT * FROM feed_items WHERE has_meta_info = 0 ORDER BY id ASC LIMIT :limit")
    fun findNewFeed(limit: Int): List<FeedItemObj>

    @Query("SELECT * FROM feed_items WHERE feed_id = :feed_id AND link = :link")
    fun findByFeed(feed_id: Long, link: String?): List<FeedItemObj>

    @Insert
    fun insertAll(vararg feedObjs: FeedItemObj)

    @Insert
    fun insert(feedObj: FeedItemObj): Long

    @Update
    fun update(feedObj: FeedItemObj)

    @Update
    fun updateAll(vararg feedObj: FeedItemObj)

    @Delete
    fun delete(feedObj: FeedItemObj)
}