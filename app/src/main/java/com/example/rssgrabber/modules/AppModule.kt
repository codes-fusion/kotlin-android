package com.example.rssgrabber.modules

import android.app.Application
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class AppModule(private var mApplication: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = mApplication
}