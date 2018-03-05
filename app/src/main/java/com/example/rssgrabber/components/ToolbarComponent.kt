package com.example.rssgrabber.components

import com.example.rssgrabber.commons.ToolbarScope
import com.example.rssgrabber.modules.ToolbarModule
import dagger.Subcomponent

@ToolbarScope
@Subcomponent(modules = arrayOf(ToolbarModule::class))
interface ToolbarComponent