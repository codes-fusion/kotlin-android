package com.example.rssgrabber

import com.example.rssgrabber.commons.ActivityScope
import com.example.rssgrabber.modules.CommonModule
import com.example.rssgrabber.modules.DBModule
import com.example.rssgrabber.modules.ListModule
import com.example.rssgrabber.modules.NetModule
import dagger.Component
import javax.inject.Singleton

@ActivityScope
@Singleton
@Component(modules = [(CommonModule::class), (NetModule::class), (DBModule::class)])
interface MainActivityUnitTestComponent {
    fun listComponent(listModule: ListModule): MainActivityUnitTestListComponent
}