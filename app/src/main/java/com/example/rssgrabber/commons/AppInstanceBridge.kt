package com.example.rssgrabber.commons

import com.example.rssgrabber.components.AppComponent

interface AppInstanceBridge {
    fun getAppComponent(): AppComponent?
    fun subscribeNetworkState(key: String, observable: NetworkObservable)
    fun disposeNetworkState(key: String)
}