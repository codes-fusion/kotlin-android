package com.example.rssgrabber.retrofit

import io.reactivex.subscribers.DisposableSubscriber
import retrofit2.HttpException

class NetworkSubscriber : DisposableSubscriber<FeedData?>() {

    var listener: LoadListener? = null
    var dataRequest: DataRequest? = null

    override fun onComplete() {}

    override fun onNext(result: FeedData?) {
        dataRequest?.let {
            val response = DataResponse(data = result, type = it.type)
            listener?.onLoadingSuccess(response)
        }
    }

    override fun onError(throwable: Throwable?) {
        var rawData: String? = null
        if (throwable is HttpException) {
            rawData = throwable.response()?.errorBody()?.string() ?: ""
        }

        dataRequest?.let {
            val response = DataResponse(rawData = rawData, type = it.type)
            listener?.onLoadingError(response)
        }
    }
}