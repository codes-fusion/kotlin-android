package com.example.rssgrabber.components

import com.example.rssgrabber.MainActivity
import com.example.rssgrabber.commons.ToolbarScope
import com.example.rssgrabber.modules.DrawerModule
import com.example.rssgrabber.modules.ToolbarModule
import dagger.Subcomponent

@ToolbarScope
@Subcomponent(modules = arrayOf(
        DrawerModule::class,
        ToolbarModule::class
))
interface DrawerComponent {
    fun inject(activity: MainActivity)
}