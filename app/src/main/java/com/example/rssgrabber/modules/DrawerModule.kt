package com.example.rssgrabber.modules

import android.support.v7.app.ActionBarDrawerToggle
import com.example.rssgrabber.MainActivity
import com.example.rssgrabber.R
import com.example.rssgrabber.application.widgets.NavigationDrawer
import com.example.rssgrabber.commons.ToolbarScope
import dagger.Module
import dagger.Provides

@Module
class DrawerModule(val activity: MainActivity) {

    @ToolbarScope
    @Provides
    fun provideDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(activity,
            activity.getDrawerLayout(), activity.getToolbar(),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
    }

    @ToolbarScope
    @Provides
    fun provideDrawer(drawerToggle: ActionBarDrawerToggle): NavigationDrawer {
        return NavigationDrawer(drawerToggle)
    }
}