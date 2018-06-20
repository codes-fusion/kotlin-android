package com.example.rssgrabber.application.models

import android.util.Log
import com.example.rssgrabber.Backend
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.entities.FeedChannel
import com.example.rssgrabber.retrofit.entities.FeedItem
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class FeedModelDataBase : FeedModel {

    override var schedulerOfSubscriber: Scheduler = Schedulers.io()
    override var service: FeedService? = null
    override var pageService: PageService? = null
    override var feed: FeedDao? = null
    override var feedItem: FeedItemDao? = null

    private fun getFeed() =
            Flowable
                .fromCallable { feed?.getFirst() }
                .flatMap { Flowable.just(it.first()) }

    private fun getChannel() =
            getFeed()
                .flatMap {
                    Flowable.just(FeedChannel().apply {
                        image = it.image
                        description = it.description
                        siteTitle = it.jobsTitle
                        divisions = it.divisions
                        title = it.name
                        divisions = it.divisions
                    })
                }

    private fun getFeedItems(request: DataRequest) =
            getFeed()
                .flatMap { it ->
                    val feedItems = if (request.id > 0) {
                        feedItem?.loadAllByIds(longArrayOf(request.id))
                    } else {
                        feedItem?.findAllByFeed(it.id, Backend.LIST_LIMIT, 0)
                    }

                    Flowable.just(feedItems?.map {
                        FeedItem(
                            id = it.id,
                            title = it.title,
                            link = it.link,
                            description = it.vacancyDescription,
                            pubDate = it.pubDate,
                            companyAbout = it.companyAbout,
                            companyLogo = it.companyLogo,
                            companyName = it.companyName,
                            vacancyDescription = it.vacancyDescription,
                            vacancyTitle = it.vacancyTitle,
                            date = it.date,
                            readyToRemote = it.readyToRemote,
                            salary = it.salary,
                            location = it.location,
                            views = it.views,
                            skils = it.skils
                        )
                    })
    }

    /**
     * Fetch feed's data from database
     * */
    override fun getData(request: DataRequest): Flowable<FeedData?>? {
        return Flowable.zip(getChannel(), getFeedItems(request), BiFunction<FeedChannel?, List<FeedItem>?, FeedData?> {
            channelResult, feedItemsResult ->

            val feedData = FeedData()
            feedData.channel = FeedChannel()
            feedData.channel?.feedItems = arrayListOf()

            feedData.channel = channelResult
            feedData.channel?.feedItems = feedItemsResult.toMutableList()

            return@BiFunction feedData
        })
        .subscribeOn(schedulerOfSubscriber)
        .onErrorReturn {
            Log.e("Network", it.message)
            FeedData()
        }
        .observeOn(AndroidSchedulers.mainThread())
    }
}