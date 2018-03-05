package com.example.rssgrabber

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.bluelinelabs.conductor.Router
import com.example.rssgrabber.commons.ActivityBridge
import com.example.rssgrabber.commons.AppInstanceBridge
import com.example.rssgrabber.components.AppComponent
import com.example.rssgrabber.utils.AndroidUtils

abstract class BaseActivity :  AppCompatActivity(), LifecycleOwner, ActivityBridge {

    private var mLifecycleRegistry: LifecycleRegistry? = null
    var mRouter: Router? = null
    var mOptionMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry?.markState(Lifecycle.State.CREATED)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        mOptionMenu = menu
        return true
    }

    /**
     * Hide option menu in custom layout
     * */
    override fun hideOptionMenu(id: Int) {
        AndroidUtils().hideOptionMenu(mOptionMenu, id)
    }

    /**
     * Show option menu in custom layout
     * */
    override fun showOptionMenu(id: Int) {
        AndroidUtils().showOptionMenu(mOptionMenu, id)
    }

    /**
     * Setup listener to option menu with custom layout
     * */
    override fun listenOptionMenu(id: Int, listener: (View) -> Unit) {
        AndroidUtils().optionMenu(mOptionMenu, id, listener)
    }

    /**
     * Setup navigation up with Back button
     * */
    override fun setHomeUp() {}

    /**
     * Setup navigation with Hamburger button
     * */
    override fun setHomeDrawer() {}

    /**
     * Toolbar title
     * */
    override fun setToolbarTitle(title: String?) {}

    /**
     * Header info of navigation
     * */
    override fun setHeader(title: String, description: String) {}

    /**
     * Navigation items
     * */
    override fun setNavigationItems(items: List<String>) {}

    /**
     * Dagger component
     * */
    override fun getComponent(): AppComponent? = (application as AppInstanceBridge).getAppComponent()

    public override fun onStart() {
        super.onStart()
        mLifecycleRegistry?.markState(Lifecycle.State.STARTED)
    }

    override fun onResume() {
        super.onResume()
        mLifecycleRegistry?.markState(Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLifecycleRegistry?.markState(Lifecycle.State.DESTROYED)
    }

    override fun getActivityLifecycle(): Lifecycle = lifecycle
    override fun getLifecycle(): LifecycleRegistry = mLifecycleRegistry!!

    override fun getToolbar(): Toolbar? = null
    override fun getDrawerLayout(): DrawerLayout? = null
    override fun getNavigationView(): NavigationView? = null
    override fun getAppInstance(): AppInstanceBridge? = application as AppInstanceBridge?
}
