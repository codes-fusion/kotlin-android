package com.example.rssgrabber.application.models

import android.util.Log
import com.example.rssgrabber.Backend
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.database.FeedItemObj
import com.example.rssgrabber.database.FeedObj
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FeedModelNetwork: FeedModel {

    override var schedulerOfSubscriber: Scheduler = Schedulers.io()
    override var service: FeedService? = null
    override var pageService: PageService? = null
    override var feed: FeedDao? = null
    override var feedItem: FeedItemDao? = null

    /**
     * Download RSS and save to database
     * */
    override fun getData(request: DataRequest): Flowable<FeedData?>? {
        return service
            ?.get(request.url ?: "")
            ?.subscribeOn(schedulerOfSubscriber)
            ?.flatMap { feedData ->
                val feeds = feed?.getFirst() ?: emptyList()
                val feedObj: FeedObj

                if (feeds.isEmpty()) {
                    feedObj = FeedObj(feedData.channel?.title)
                    feedObj.id = feed?.insert(feedObj) ?: 0
                } else {
                    feedObj = feeds.first()
                }

                feedData.channel?.feedItems?.forEachIndexed { i, item ->
                    if (i > Backend.LIST_LIMIT - 1) return@forEachIndexed
                    val feedItems = feedItem?.findByFeed(feedObj.id, item.link)

                    feedItems?.isEmpty()?.let {
                        val feedItemObj = FeedItemObj(
                            description = feedObj.description,
                            link = item.link,
                            pubDate = item.pubDate,
                            title = item.title,
                            feedId = feedObj.id
                        )
                        feedItemObj.id = feedItem?.insert(feedItemObj) ?: 0
                    }
                }

                // feedItem?.updateAll(*bulkUpdate.toTypedArray())
                // bulkUpdate.clear()

                feedData.network = true

                return@flatMap Flowable.just(feedData)
            }
            ?.onErrorReturn {
                Log.e("Network", it.message)
                FeedData()
            }
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}