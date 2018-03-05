package com.example.rssgrabber.commons

import android.arch.lifecycle.Lifecycle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.rssgrabber.components.AppComponent

interface ActivityBridge {
    fun getComponent(): AppComponent?
    fun getToolbar(): Toolbar?
    fun getDrawerLayout(): DrawerLayout?
    fun getNavigationView(): NavigationView?
    fun getAppInstance(): AppInstanceBridge?
    fun setHomeUp()
    fun setHomeDrawer()
    fun showOptionMenu(id: Int)
    fun hideOptionMenu(id: Int)
    fun listenOptionMenu(id: Int, listener: (View) -> Unit)
    fun setToolbarTitle(title: String?)
    fun setHeader(title: String, description: String)
    fun setNavigationItems(items: List<String>)
    fun getActivityLifecycle(): Lifecycle
}
