package com.example.rssgrabber.retrofit

interface LoadListener {
    fun onLoadingStart(dataRequest: DataRequest)
    fun onLoadingSuccess(dataResponse: DataResponse)
    fun onLoadingError(dataResponse: DataResponse)
}