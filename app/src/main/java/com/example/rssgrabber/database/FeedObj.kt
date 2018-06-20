package com.example.rssgrabber.database

import android.arch.persistence.room.*

@Entity(tableName = "feeds", indices = [(Index("name"))])
data class FeedObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "jobs_title")
    var jobsTitle: String,

    @ColumnInfo(name = "site_title")
    var siteTitle: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "divisions")
    var divisions: String
) {
    @Ignore
    constructor(name: String?) : this(
        id = 0,
        name = name ?: "",
        description = "",
        divisions = "",
        image = "",
        jobsTitle = "",
        siteTitle = ""
    )
}