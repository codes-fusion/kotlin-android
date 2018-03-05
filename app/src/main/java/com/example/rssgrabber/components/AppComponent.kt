package com.example.rssgrabber.components

import com.example.rssgrabber.SplashActivity
import com.example.rssgrabber.commons.ActivityScope
import com.example.rssgrabber.modules.*
import dagger.Component
import javax.inject.Singleton

@ActivityScope
@Singleton
@Component(modules = arrayOf(
    CommonModule::class,
    NetModule::class,
    DBModule::class
))
interface AppComponent {
    fun drawerComponent(drawer: DrawerModule, toolbarModule: ToolbarModule): DrawerComponent
    fun toolbarComponent(toolbarModule: ToolbarModule): ToolbarComponent
    fun listComponent(listModule: ListModule): ListComponent
    fun networkStateComponent(networkStateModule: NetworkStateModule): NetworkStateComponent
    fun inject(splash: SplashActivity)
}