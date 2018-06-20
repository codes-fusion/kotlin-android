package com.example.rssgrabber.application.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.example.rssgrabber.application.models.FeedModel
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.DataResponse
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.LoadListener
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedPresenterImpl : ViewModel(), FeedPresenter, LoadListener {

    private var mLiveData = MutableLiveData<Any>()
    private val mCompositeSubscription: CompositeDisposable? = CompositeDisposable()

    private var mRequestBlock = false
    private var mHasValue = false
    private var mSchedulerOfSubscriber: Scheduler = Schedulers.single()
    private var mSubscription: Disposable? = null

    override var databaseModel: FeedModel? = null
    override var networkModel: FeedModel? = null
    override var additionalModel: FeedModel? = null 

    /**
     * Fetch list
     *
     * @param request Request object
     * @param force Need for refreshing
     * */
    override fun fetchFeedList(request: DataRequest, force: Boolean): Boolean {
        if (mRequestBlock) return false
        when {
            !mHasValue || force -> {
                mRequestBlock = true
                mSubscription?.dispose()

                mSubscription = subscriptionHandler(request) {
                    Flowable.concat(
                            networkModel?.getData(request),
                            additionalModel?.getData(request),
                            databaseModel?.getData(request))
                            .subscribeOn(mSchedulerOfSubscriber)
                            .takeLast(1)
                }

                mCompositeSubscription?.add(mSubscription!!)
                return true
            }
        }

        return false
    }

    override fun flushBackgroundThreads() {}

    /**
     * Fetch feed's details
     * @param request Request object
     * */
    override fun fetchFeedDetail(request: DataRequest) {
        if (mRequestBlock) return
        val response = mLiveData.value as DataResponse?
        val feedItem = response?.data as FeedData?
        val currentId = feedItem?.channel?.feedItems?.firstOrNull() ?: 0

        when {
            !mHasValue || currentId != request.id -> {
                mRequestBlock = true
                mSubscription?.dispose()

                mSubscription = subscriptionHandler(request) {
                    databaseModel
                            ?.getData(request)
                            ?.subscribeOn(mSchedulerOfSubscriber)
                }

                mCompositeSubscription?.add(mSubscription!!)
            }
        }
    }

    private fun subscriptionHandler(request: DataRequest, subject: () -> Flowable<FeedData?>?) =
        subject()?.doOnError {
            val response = DataResponse(type = request.type)
            onLoadingError(response)
        }?.subscribe {
            val response = DataResponse(data = it, type = request.type)
            onLoadingSuccess(response)
        }

    /**
     * Before loading
     * @param dataRequest Request object
     * */
    override fun onLoadingStart(dataRequest: DataRequest) {}

    /**
     * Display data after loading complete
     * @param dataResponse Response object
     * */
    override fun onLoadingSuccess(dataResponse: DataResponse) {
        mRequestBlock = false
        mLiveData.postValue(dataResponse)
        mHasValue = true
    }

    /**
     * Display error message when load fail
     * @param dataResponse Response object
     * */
    override fun onLoadingError(dataResponse: DataResponse) {
        mRequestBlock = false
        mLiveData.postValue(dataResponse)
    }

    /**
     * Subscribe to LiveData
     * */
    override fun observeFeed(owner: LifecycleOwner, observer: Observer<Any>) {
        mLiveData.observeForever(observer)
    }

    /**
     * Stop loading when presenter destroy
     * */
    override fun onCleared() {
        mHasValue = false
        mSubscription?.dispose()
        mCompositeSubscription?.clear()
    }
}
