package com.example.rssgrabber.modules

import android.content.Context
import com.example.rssgrabber.commons.NetworkStateScope
import com.example.rssgrabber.utils.AndroidUtils
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Module
class NetworkStateModule(private var context: Context) {

    @NetworkStateScope
    @Provides
    fun provideNetworkState(): Flowable<Boolean> {
        return Flowable.interval(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
            .flatMap({ Flowable.just(AndroidUtils().isNetworkAvailable(context)) })
            .observeOn(AndroidSchedulers.mainThread())
    }
}