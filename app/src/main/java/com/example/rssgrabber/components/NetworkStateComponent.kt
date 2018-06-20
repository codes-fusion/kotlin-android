package com.example.rssgrabber.components

import com.example.rssgrabber.AppInstance
import com.example.rssgrabber.commons.NetworkStateScope
import com.example.rssgrabber.modules.NetworkStateModule
import dagger.Subcomponent

@NetworkStateScope
@Subcomponent(modules = [(NetworkStateModule::class)])
interface NetworkStateComponent {
    fun inject(app: AppInstance)
}