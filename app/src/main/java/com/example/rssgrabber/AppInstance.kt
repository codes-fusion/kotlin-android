package com.example.rssgrabber

import android.content.Context
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.example.rssgrabber.commons.AppInstanceBridge
import com.example.rssgrabber.commons.NetworkObservable
import com.example.rssgrabber.components.AppComponent
import com.example.rssgrabber.components.DaggerAppComponent
import com.example.rssgrabber.modules.NetworkStateModule
import com.example.rssgrabber.utils.Fonts
import io.reactivex.Flowable
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

open class AppInstance : MultiDexApplication(), AppInstanceBridge {

    companion object {
        val DEBUG = true
        var TEST = false
        var appContext: Context? = null
        var fonts: Fonts? = null
    }

    private var mAppComponent: AppComponent? = null
    private var mNetworkObservables: MutableMap<String, NetworkObservable>? = null

    @Inject
    lateinit var networkState: Flowable<Boolean>

    operator fun get(context: Context): AppInstanceBridge = context.applicationContext as AppInstanceBridge

    override fun onCreate() {
        super.onCreate()
        fonts = Fonts(applicationContext)
        appContext = applicationContext
        mAppComponent = DaggerAppComponent.create()
        mAppComponent
            ?.networkStateComponent(NetworkStateModule(this))
            ?.inject(this)

        RxJavaPlugins.setErrorHandler { Log.e("RxJavaPlugins", "Catch error") }

        mNetworkObservables = mutableMapOf()
        networkState.subscribe { state ->
            mNetworkObservables?.forEach {
                it.value.onNetworkStateChanged(state)
            }
        }
    }

    override fun getAppComponent(): AppComponent? = mAppComponent

    override fun subscribeNetworkState(key: String, observable: NetworkObservable) {
        mNetworkObservables?.put(key, observable)
    }

    override fun disposeNetworkState(key: String) {
        mNetworkObservables?.remove(key)
    }

    override fun attachBaseContext(base: Context?) {
        try {
            super.attachBaseContext(base)
        } catch (ignored: RuntimeException) {}
    }
}
