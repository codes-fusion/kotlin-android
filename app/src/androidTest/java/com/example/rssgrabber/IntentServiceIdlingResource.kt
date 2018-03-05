package com.example.rssgrabber

import android.content.Context
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.IdlingResource.ResourceCallback

class IntentServiceIdlingResource(private val context: Context) : IdlingResource {

    private var startTime: Long = 0
    private var waitingTime: Long = 0
    private var resourceCallback: ResourceCallback? = null

    var isIntentServiceRunning: Boolean = false

    init {
        startTime = System.currentTimeMillis()
        waitingTime = 10000
    }

    override fun getName(): String {
        return IntentServiceIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        if (!isIntentServiceRunning) {
            resourceCallback?.onTransitionToIdle()
        }
        
        return true
    }

    override fun registerIdleTransitionCallback(resourceCallback: ResourceCallback) {
        this.resourceCallback = resourceCallback
    }
}