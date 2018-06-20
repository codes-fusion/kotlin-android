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

class FeedModelMisc : FeedModel {

    // override var schedulerOfSubscriber: Scheduler = Schedulers.from(Executors.newFixedThreadPool(5))
    override var schedulerOfSubscriber: Scheduler = Schedulers.io()
    override var service: FeedService? = null
    override var pageService: PageService? = null
    override var feed: FeedDao? = null
    override var feedItem: FeedItemDao? = null

    private fun getFeed() = Flowable
            .fromCallable { feed?.getFirst() }
            .flatMap { Flowable.just(it.first()) }

    private fun getChannel() = getFeed()
            .flatMap { feedData ->
                pageService
                    ?.get(Backend.MAIN_PAGE)
                    ?.flatMap {
                        feed?.updateAll(feedData.apply {
                            description = it.description ?: ""
                            image = it.image ?: ""
                            jobsTitle = it.jobsTitle ?: ""
                            siteTitle = it.title ?: ""
                            divisions = it.divisions?.joinToString(", ")  ?: ""
                        })

                        Flowable.just(FeedChannel().apply {
                            image = feedData.image
                            description = feedData.description
                            siteTitle = feedData.jobsTitle
                            divisions = feedData.divisions
                            title = feedData.name
                            divisions = feedData.divisions
                        })
                    }
            }

    private fun getFeedItems() = getFeed()
            .flatMap { Flowable.just(feedItem?.findNewFeed(Backend.LIST_LIMIT)) }
            .flatMap { feedItems ->
                val result = arrayListOf<FeedItem>()
                feedItems.forEach { item ->
                    pageService
                        ?.getVacancy(item.link)
                        ?.blockingSubscribe {
                            feedItem?.update(item.apply {
                                companyAbout = it.companyAbout ?: ""
                                companyLogo = it.companyImage ?: ""
                                companyName = it.companyName ?: ""
                                vacancyDescription = it.vacancyDescription ?: ""
                                vacancyTitle = it.vacancyTitle ?: ""
                                date = it.date ?: ""
                                readyToRemote = it.readyToRemote ?: ""
                                salary = it.salary ?: ""
                                location = it.location ?: ""
                                views = it.views ?: ""
                                skils = it.skils?.joinToString(", ") ?: ""
                                hasMetaInfo = 1
                            })

                            result.add(FeedItem(
                                id = item.id,
                                title = item.vacancyTitle,
                                link = item.link,
                                description = item.vacancyDescription,
                                pubDate = item.pubDate,
                                companyAbout = item.companyAbout,
                                companyLogo = item.companyLogo,
                                companyName = item.companyName,
                                vacancyDescription = item.vacancyDescription,
                                vacancyTitle = item.vacancyTitle,
                                date = item.date,
                                readyToRemote = item.readyToRemote,
                                salary = item.salary,
                                location = item.location,
                                views = item.views,
                                skils = item.skils
                            ))
                        }
                }

                return@flatMap Flowable.just(result)
            }

    /**
     * Load more feed's information from web page and save to database
     * */
    override fun getData(request: DataRequest): Flowable<FeedData?>? {
        return Flowable.zip(getChannel(), getFeedItems(), BiFunction<FeedChannel, List<FeedItem>, FeedData?> {
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