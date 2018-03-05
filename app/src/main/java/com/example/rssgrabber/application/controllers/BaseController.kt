package com.example.rssgrabber.application.controllers

import android.arch.lifecycle.*
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.example.rssgrabber.application.decorators.ScaleFadeChangeHandler
import com.example.rssgrabber.commons.ActivityBridge
import com.example.rssgrabber.commons.AppInstanceBridge

abstract class BaseController: Controller(), LifecycleOwner,
        LifecycleObserver, SwipeRefreshLayout.OnRefreshListener {

    private var mLifecycleRegistry: LifecycleRegistry? = null

    init {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry?.markState(Lifecycle.State.CREATED)
    }

    var activityBridge: ActivityBridge? = null
        get() = activity as ActivityBridge?

    var appBridge: AppInstanceBridge? = null
        get() = (activity as ActivityBridge?)?.getAppInstance()

    override fun getLifecycle(): Lifecycle = mLifecycleRegistry!!

    override fun onAttach(view: View) {
        super.onAttach(view)
        mLifecycleRegistry?.markState(Lifecycle.State.STARTED)
        mLifecycleRegistry?.markState(Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLifecycleRegistry?.markState(Lifecycle.State.DESTROYED)
    }

    override fun onRefresh() {}

    fun launch(controller: Controller) {
        val routerTransaction = RouterTransaction.with(controller)
        routerTransaction.pushChangeHandler(ScaleFadeChangeHandler())
        routerTransaction.popChangeHandler(ScaleFadeChangeHandler())
        router?.pushController(routerTransaction)
    }

    fun observeActivity() {
        val activity = (activity as ActivityBridge)
        val lifeCycle = activity.getActivityLifecycle()
        val created = lifeCycle.currentState.isAtLeast(Lifecycle.State.CREATED)
        if (created) {
            lifeCycle.addObserver(this)
        }
    }

    fun removeObservingActivity() {
        val activity = (activity as ActivityBridge)
        val lifeCycle = activity.getActivityLifecycle()
        lifeCycle.removeObserver(this)
    }

    fun getTag(): String = this.javaClass.name

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onLifeCycleResume() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onLifeCyclePause() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onLifeCycleDestroy() {}
}