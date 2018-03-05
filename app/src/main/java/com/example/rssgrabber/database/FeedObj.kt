package com.example.rssgrabber.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "feeds", indices = arrayOf(Index("name")))
data class FeedObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "jobs_title")
    var jobsTitle: String? = null,

    @ColumnInfo(name = "site_title")
    var siteTitle: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "divisions")
    var divisions: String? = null
)