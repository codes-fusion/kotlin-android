package com.example.rssgrabber.database

import android.arch.persistence.room.*

@Entity(
    tableName = "feed_items",
    indices = [
        (Index("pub_date")),
        (Index("feed_id")),
        (Index(value = arrayOf("feed_id", "link"), unique = true))
    ],
    foreignKeys = [
        (ForeignKey(entity = FeedObj::class, parentColumns = arrayOf("id"), childColumns = arrayOf("feed_id")))
    ]
)
data class FeedItemObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "pub_date")
    var pubDate: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "link")
    var link: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "has_meta_info")
    var hasMetaInfo: Int,

    @ColumnInfo(name = "feed_id")
    var feedId: Long,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "views")
    var views: String,

    @ColumnInfo(name = "location")
    var salary: String,

    @ColumnInfo(name = "salary")
    var location: String,

    @ColumnInfo(name = "ready_to_remote")
    var readyToRemote: String,

    @ColumnInfo(name = "company_name")
    var companyName: String,

    @ColumnInfo(name = "company_about")
    var companyAbout: String,

    @ColumnInfo(name = "company_logo")
    var companyLogo: String,

    @ColumnInfo(name = "skils")
    var skils: String,

    @ColumnInfo(name = "company_url")
    var companyUrl: String,

    @ColumnInfo(name = "vacancy_description")
    var vacancyDescription: String,

    @ColumnInfo(name = "vacancy_title")
    var vacancyTitle: String
) {
    @Ignore
    constructor(description: String?, link: String?, pubDate: String?, title: String?, feedId: Long) : this(
        id = 0,
        feedId = feedId,
        description = description ?: "",
        title = title ?: "",
        link = link ?: "",
        date = pubDate ?: "",
        pubDate = pubDate ?: "",
        companyAbout = "",
        companyLogo = "",
        companyName = "",
        companyUrl = "",
        hasMetaInfo = 0,
        location = "",
        readyToRemote = "",
        salary = "",
        skils = "",
        vacancyDescription = "",
        vacancyTitle = "",
        views = ""
    )
}