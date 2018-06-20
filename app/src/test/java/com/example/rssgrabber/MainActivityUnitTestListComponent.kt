package com.example.rssgrabber

import com.example.rssgrabber.commons.ListScope
import com.example.rssgrabber.modules.ListModule
import dagger.Subcomponent

@ListScope
@Subcomponent(modules = [(ListModule::class)])
interface MainActivityUnitTestListComponent {
    fun inject(mainController: MainActivityUnitTest)
}