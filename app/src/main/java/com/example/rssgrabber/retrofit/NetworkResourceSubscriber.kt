package com.example.rssgrabber.retrofit

import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException

open class NetworkResourceSubscriber :
        ResourceSubscriber<Any>() {

    var listener: LoadListener? = null
    var dataRequest: DataRequest? = null

    override fun onStart() {
        request(Long.MAX_VALUE)
        dataRequest?.let {
            listener?.onLoadingStart(it)
        }
    }

    override fun onError(throwable: Throwable) {
        var rawData: String? = null
        if (throwable is HttpException) {
            rawData = throwable.response()?.errorBody()?.string() ?: ""
        }

        dataRequest?.let {
            val response = DataResponse(rawData = rawData, type = it.type)
            listener?.onLoadingError(response)
        }
    }

    override fun onNext(result: Any) {
        dataRequest?.let {
            val response = DataResponse(data = result, type = it.type)
            listener?.onLoadingSuccess(response)
        }
    }

    override fun onComplete() {}
}