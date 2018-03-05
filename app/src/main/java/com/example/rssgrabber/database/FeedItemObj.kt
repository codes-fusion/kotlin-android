package com.example.rssgrabber.database

import android.arch.persistence.room.*

@Entity(
    tableName = "feed_items",
    indices = arrayOf(Index("pub_date"), Index("feed_id"), Index(value = *arrayOf("feed_id", "link"), unique = true)),
    foreignKeys = arrayOf(ForeignKey(entity = FeedObj::class, parentColumns = arrayOf("id"), childColumns = arrayOf("feed_id")))
)
data class FeedItemObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "pub_date")
    var pubDate: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "link")
    var link: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "has_meta_info")
    var hasMetaInfo: Int = 0,

    @ColumnInfo(name = "feed_id")
    var feedId: Long = 0,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "views")
    var views: String? = null,

    @ColumnInfo(name = "location")
    var salary: String? = null,

    @ColumnInfo(name = "salary")
    var location: String? = null,

    @ColumnInfo(name = "ready_to_remote")
    var readyToRemote: String? = null,

    @ColumnInfo(name = "company_name")
    var companyName: String? = null,

    @ColumnInfo(name = "company_about")
    var companyAbout: String? = null,

    @ColumnInfo(name = "company_logo")
    var companyLogo: String? = null,

    @ColumnInfo(name = "skils")
    var skils: String? = null,

    @ColumnInfo(name = "company_url")
    var companyUrl: String? = null,

    @ColumnInfo(name = "vacancy_description")
    var vacancyDescription: String? = null,

    @ColumnInfo(name = "vacancy_title")
    var vacancyTitle: String? = null
)