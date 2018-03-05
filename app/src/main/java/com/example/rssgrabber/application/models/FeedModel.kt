package com.example.rssgrabber.application.models

import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import io.reactivex.Flowable
import io.reactivex.Scheduler

interface FeedModel {
    var schedulerOfSubscriber: Scheduler
    var service: FeedService?
    var pageService: PageService?
    var feed: FeedDao?
    var feedItem: FeedItemDao?
    fun getData(request: DataRequest): Flowable<FeedData?>?
}