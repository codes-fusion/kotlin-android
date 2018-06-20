package com.example.rssgrabber

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rssgrabber.application.controllers.MainController
import com.example.rssgrabber.application.widgets.NavigationDrawer
import com.example.rssgrabber.commons.NetworkObservable
import com.example.rssgrabber.modules.DrawerModule
import com.example.rssgrabber.modules.ToolbarModule
import com.example.rssgrabber.utils.AndroidUtils
import com.example.rssgrabber.utils.ToolbarUtils
import kotterknife.bindOptionalView
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, NetworkObservable {

    private val mContainer: ViewGroup? by bindOptionalView(R.id.container)
    private val mToolbarView: Toolbar? by bindOptionalView(R.id.toolbar)
    private val mDrawerLayout: DrawerLayout? by bindOptionalView(R.id.drawer_layout)
    private val mNavigationView: NavigationView? by bindOptionalView(R.id.nav_view)

    @Inject
    lateinit var utils: AndroidUtils

    @Inject
    lateinit var toolbar: ToolbarUtils

    @Inject
    lateinit var navigationDrawer: NavigationDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbarView)

        val toolbarModule = ToolbarModule(this)
        val drawerModule = DrawerModule(this)

        getComponent()
            ?.drawerComponent(drawerModule, toolbarModule)
            ?.inject(this)

        title = ""
        navigationDrawer.addSupportActionBar(supportActionBar)
        navigationDrawer.addDrawerLayout(mDrawerLayout)
        navigationDrawer.syncState()

        mNavigationView?.setNavigationItemSelectedListener(this)
        mRouter = utils.attachMainController(this, mContainer,
                savedInstanceState, MainController())
    }

    override fun getToolbar(): Toolbar? = mToolbarView
    override fun getDrawerLayout(): DrawerLayout? = mDrawerLayout
    override fun getNavigationView(): NavigationView? = mNavigationView

    /**
     * Setup navigation with back button
     * */
    override fun setHomeUp() {
        navigationDrawer.setHomeUp {
            onBackPressed()
        }
    }

    /**
     * Setup navigation with hamburger button
     * */
    override fun setHomeDrawer() {
        navigationDrawer.setHomeDefault()
    }

    /**
     * Toolbar title
     * */
    override fun setToolbarTitle(title: String?) {
        toolbar.flipTextView(title)
    }

    /**
     * Header info of navigation
     * */
    override fun setHeader(title: String, description: String) {
        val headerView = mNavigationView?.getHeaderView(0)
        headerView?.findViewById<TextView>(R.id.header_title)?.text = title
        headerView?.findViewById<TextView>(R.id.header_description)?.text = description
    }

    /**
     * Navigation items
     * */
    override fun setNavigationItems(items: List<String>) {
        val menu = mNavigationView?.menu
        menu?.clear()
        items.forEachIndexed { i, it ->
            val menuItem = menu?.add(Menu.NONE, i, Menu.NONE, it)
            menuItem?.setIcon(R.drawable.ic_menu_slideshow)
            menu?.setGroupCheckable(menuItem?.groupId ?: 0, true, true)
        }
    }

    /**
     * Setup option menu with custom layout
     * */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        val actionViewItem = menu?.findItem(R.id.action_settings)
        val view = actionViewItem?.actionView
        val stateView = view?.findViewById<View?>(R.id.network_state)
        val frameAnimation = stateView?.background as AnimationDrawable?

        stateView?.visibility = View.VISIBLE
        frameAnimation?.setEnterFadeDuration(400)
        frameAnimation?.setExitFadeDuration(400)
        frameAnimation?.start()
        return true
    }

    override fun onResume() {
        super.onResume()
        getAppInstance()?.subscribeNetworkState(javaClass.name, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        getAppInstance()?.subscribeNetworkState(javaClass.name, this)
    }

    /**
     * When network connection changes
     * */
    override fun onNetworkStateChanged(state: Boolean) {}

    override fun onBackPressed() {
        val drawerOpen = mDrawerLayout?.isDrawerOpen(GravityCompat.START) ?: false
        val handleBack = mRouter?.handleBack() ?: false
        when {
            drawerOpen -> mDrawerLayout?.closeDrawer(GravityCompat.START)
            !handleBack -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {}
        mDrawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}
