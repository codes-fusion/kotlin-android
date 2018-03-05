package com.example.rssgrabber.application.interactors

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.example.rssgrabber.application.models.FeedModel
import com.example.rssgrabber.retrofit.DataRequest

interface FeedInteractor {
    var databaseModel: FeedModel?
    var networkModel: FeedModel?
    var additionalModel: FeedModel?

    fun fetchFeedList(request: DataRequest, force: Boolean = false): Boolean
    fun fetchFeedDetail(request: DataRequest)
    fun flushBackgroundThreads()
    fun observeFeed(owner: LifecycleOwner, observer: Observer<Any>)
}