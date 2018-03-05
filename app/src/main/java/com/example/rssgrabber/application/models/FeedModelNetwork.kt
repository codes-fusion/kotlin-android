package com.example.rssgrabber.application.models

import com.example.rssgrabber.Backend
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.database.FeedItemObj
import com.example.rssgrabber.database.FeedObj
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.entities.FeedItem
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

    private fun objFromFeedItem(obj: FeedItemObj, item: FeedItem) = with(obj) {
        description = item.description
        link = item.link
        pubDate = item.pubDate
        title = item.title
    }

    /**
     * Load RSS and save it to database
     *
     * @return feed's list as Flowable object
     * */
    override fun getData(request: DataRequest): Flowable<FeedData?>? {
        val url = request.url ?: ""
        var networkData: FeedData? = null

        return service?.get(url)
            ?.subscribeOn(schedulerOfSubscriber)
            ?.flatMap { item ->
                networkData = item
                networkData?.network = true
                return@flatMap Flowable.just(feed?.getFirst())
            }
            ?.flatMap { items ->
                when {
                    items.isEmpty() -> {
                        val feedObj = FeedObj()
                        feedObj.name = networkData?.channel?.title
                        feedObj.id = feed?.insert(feedObj) ?: 0
                        return@flatMap Flowable.just(feedObj)
                    }
                    else -> return@flatMap Flowable.just(items.first())
                }
            }
            ?.flatMap { item ->
                val updateBulk = mutableListOf<FeedItemObj>()
                networkData?.channel?.feedItems?.forEachIndexed { i, it ->
                    if (i > Backend.LIST_LIMIT - 1) return@forEachIndexed
                    val items = feedItem?.findByFeed(item.id, it.link)
                    val isEmpty = items?.isEmpty() ?: true
                    when {
                        isEmpty -> {
                            val feedItemObj = FeedItemObj()
                            objFromFeedItem(feedItemObj, it)
                            feedItemObj.feedId = item.id
                            feedItemObj.id = feedItem?.insert(feedItemObj) ?: 0
                        }
                        else -> {
                            items?.firstOrNull()?.let {
                                updateBulk.add(it)
                            }
                        }
                    }
                }

                feedItem?.updateAll(*updateBulk.toTypedArray())
                updateBulk.clear()

                return@flatMap Flowable.just(networkData)
            }
            ?.onErrorReturn {
                return@onErrorReturn FeedData()
            }
            ?.observeOn(AndroidSchedulers.mainThread())
    }
}