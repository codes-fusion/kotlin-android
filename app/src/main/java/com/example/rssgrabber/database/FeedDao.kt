package com.example.rssgrabber.database

import android.arch.persistence.room.*

@Dao
interface FeedDao {
    @Query("SELECT * FROM feeds")
    fun getAll(): List<FeedObj>

    @Query("SELECT * FROM feeds LIMIT 1")
    fun getFirst(): List<FeedObj>

    @Query("SELECT * FROM feeds WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<FeedObj>

    @Query("SELECT * FROM feeds WHERE name LIKE :name LIMIT 1")
    fun findByTitle(name: String): List<FeedObj>

    @Insert
    fun insertAll(vararg feedObjs: FeedObj)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feedObj: FeedObj): Long

    @Update
    fun update(feedObj: FeedObj): Int

    @Update
    fun updateAll(vararg feedObj: FeedObj)

    @Delete
    fun delete(feedObj: FeedObj)
}