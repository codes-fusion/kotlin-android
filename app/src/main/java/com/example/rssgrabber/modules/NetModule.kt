package com.example.rssgrabber.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.rssgrabber.AppInstance
import com.example.rssgrabber.retrofit.interceptors.HeaderInterceptor
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun providesApplication(): Context? = AppInstance.appContext

    @Provides
    @Singleton
    fun providesSharedPreferences(context: Context?): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideOkHttpCache(context: Context?): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context?.cacheDir!!, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
        builder.connectTimeout(60000, TimeUnit.MILLISECONDS)
        builder.addInterceptor(HeaderInterceptor())

        if (AppInstance.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://example.com")
                .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideService(builder: Retrofit.Builder): FeedService {
        val retrofit: Retrofit = builder
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        return retrofit.create(FeedService::class.java)
    }

    @Provides
    @Singleton
    fun provideSiteService(builder: Retrofit.Builder): PageService {
        val retrofit: Retrofit = builder
                .addConverterFactory(JspoonConverterFactory.create())
                .build()

        return retrofit.create(PageService::class.java)
    }
}
