package com.example.rssgrabber.components

import com.example.rssgrabber.application.controllers.MainController
import com.example.rssgrabber.commons.ListScope
import com.example.rssgrabber.modules.ListModule
import dagger.Subcomponent

@ListScope
@Subcomponent(modules = arrayOf(ListModule::class))
interface ListComponent {
    fun inject(mainController: MainController)
}