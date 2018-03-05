package com.example.rssgrabber.modules

import com.example.rssgrabber.utils.AndroidUtils
import dagger.Module
import dagger.Provides

@Module
class CommonModule {

    @Provides
    fun provideUtils() = AndroidUtils()
}