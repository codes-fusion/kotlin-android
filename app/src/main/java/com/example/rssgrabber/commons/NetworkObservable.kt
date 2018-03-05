package com.example.rssgrabber.commons

interface NetworkObservable {
    fun onNetworkStateChanged(state: Boolean)
}