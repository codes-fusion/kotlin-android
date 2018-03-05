package com.example.rssgrabber.application.models

import com.example.rssgrabber.Backend
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.database.FeedItemObj
import com.example.rssgrabber.database.FeedObj
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.entities.FeedChannel
import com.example.rssgrabber.retrofit.entities.FeedItem
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FeedModelAdditional : FeedModel {

    // override var schedulerOfSubscriber: Scheduler = Schedulers.from(Executors.newFixedThreadPool(5))
    override var schedulerOfSubscriber: Scheduler = Schedulers.io()
    override var service: FeedService? = null
    override var pageService: PageService? = null
    override var feed: FeedDao? = null
    override var feedItem: FeedItemDao? = null

    private fun feedItemFromObj(item: FeedItem, obj: FeedItemObj) = with(item) {
        id = obj.id
        title = obj.title
        link = obj.link
        description = obj.description
        pubDate = obj.pubDate
        companyAbout = obj.companyAbout
        companyLogo = obj.companyLogo
        companyName = obj.companyName
        vacancyDescription = obj.vacancyDescription
        vacancyTitle = obj.vacancyTitle
        date = obj.date
        readyToRemote = obj.readyToRemote
        salary = obj.salary
        location = obj.location
        views = obj.views
        skils = obj.skils
    }

    private fun feedFromObj(item: FeedData, obj: FeedObj) = item.channel?.apply {
        image = obj.image
        description = obj.description
        siteTitle = obj.jobsTitle
        divisions = obj.divisions
        title = obj.name
        divisions = obj.divisions
    }

    /**
     * Load more feed's information from
     * web page and save to database
     *
     * @return feed's list as Flowable object
     * */
    override fun getData(request: DataRequest): Flowable<FeedData?>? {
        val feedData = FeedData()
        feedData.channel = FeedChannel()
        feedData.channel?.feedItems = arrayListOf()

        return Flowable.fromCallable { feed?.getFirst() }
            ?.subscribeOn(schedulerOfSubscriber)
            ?.flatMap { it ->
                val item = it.first()
                pageService
                    ?.get(Backend.MAIN_PAGE)
                    ?.subscribe {
                        item.description = it.description
                        item.image = it.image
                        item.jobsTitle = it.jobsTitle
                        item.siteTitle = it.title
                        item.divisions = it.divisions?.joinToString(", ")
                        feedFromObj(feedData, item)
                    }

                feed?.updateAll(item)
                return@flatMap Flowable.just(feedItem?.findNewFeed(Backend.LIST_LIMIT))
            }
            ?.flatMap { items ->
                items.forEach {
                    val feed = it
                    pageService
                        ?.getVacancy(feed.link ?: "")
                        ?.subscribe {
                            feed.apply {
                                companyAbout = it.companyAbout
                                companyLogo = it.companyImage
                                companyName = it.companyName
                                vacancyDescription = it.vacancyDescription
                                vacancyTitle = it.vacancyTitle
                                date = it.date
                                readyToRemote = it.readyToRemote
                                salary = it.salary
                                location = it.location
                                views = it.views
                                skils = it.skils?.joinToString(", ")
                                hasMetaInfo = 1
                            }
                        }

                    feedItem?.update(feed)

                    val feedItem = FeedItem()
                    feedItemFromObj(feedItem, it)
                    feedData.channel?.feedItems?.add(feedItem)
                }
                return@flatMap Flowable.just(items)
            }
            ?.flatMap {
                return@flatMap Flowable.just(feedData)
            }
            ?.onErrorReturn { return@onErrorReturn FeedData() }
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}