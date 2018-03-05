package com.example.rssgrabber.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.example.rssgrabber.application.adapters.ViewAdapterImpl
import com.example.rssgrabber.application.adapters.ViewAdapterItem
import com.example.rssgrabber.application.interactors.FeedInteractor
import com.example.rssgrabber.application.interactors.FeedInteractorImpl
import com.example.rssgrabber.application.models.FeedModelAdditional
import com.example.rssgrabber.application.models.FeedModelDataBase
import com.example.rssgrabber.application.models.FeedModelNetwork
import com.example.rssgrabber.commons.ListScope
import com.example.rssgrabber.database.FeedDao
import com.example.rssgrabber.database.FeedItemDao
import com.example.rssgrabber.retrofit.NetworkResourceSubscriber
import com.example.rssgrabber.retrofit.NetworkSubscriber
import com.example.rssgrabber.retrofit.services.FeedService
import com.example.rssgrabber.retrofit.services.PageService
import dagger.Module
import dagger.Provides

@Module
class ListModule(private var activity: FragmentActivity, private var tag: String?) {

    @ListScope
    @Provides
    fun provideListAdapter(): ViewAdapterImpl<ViewAdapterItem> = ViewAdapterImpl()

    @ListScope
    @Provides
    fun provideNetworkResourceSubscriber(): NetworkResourceSubscriber = NetworkResourceSubscriber()

    @ListScope
    @Provides
    fun provideNetworkSubscriber(): NetworkSubscriber = NetworkSubscriber()

    @ListScope
    @Provides
    fun provideFeedModelNetwork(
            feedItem: FeedItemDao, feed: FeedDao,
            mService: FeedService, mPageService: PageService):
            FeedModelNetwork {

        val model = FeedModelNetwork()
        model.feed = feed
        model.feedItem = feedItem
        model.service = mService
        model.pageService = mPageService
        return model
    }

    @ListScope
    @Provides
    fun provideFeedModelAdditional(
            feedItem: FeedItemDao, feed: FeedDao,
            mService: FeedService, mPageService: PageService):
            FeedModelAdditional {

        val model = FeedModelAdditional()
        model.feed = feed
        model.feedItem = feedItem
        model.service = mService
        model.pageService = mPageService
        return model
    }

    @ListScope
    @Provides
    fun provideFeedModelDataBase(
            feedItem: FeedItemDao, feed: FeedDao,
            mService: FeedService, mPageService: PageService):
            FeedModelDataBase {

        val model = FeedModelDataBase()
        model.feed = feed
        model.feedItem = feedItem
        model.service = mService
        model.pageService = mPageService
        return model
    }

    @ListScope
    @Provides
    fun provideInteractor(
            network: FeedModelNetwork,
            additional: FeedModelAdditional,
            database: FeedModelDataBase,
            networkSubscriber: NetworkSubscriber):
            FeedInteractor {

        val presenter = ViewModelProviders
            .of(activity)
            .get(tag ?: "DEFAULT", FeedInteractorImpl::class.java)

        presenter.additionalModel = presenter.additionalModel ?: additional
        presenter.networkModel = presenter.networkModel ?: network
        presenter.databaseModel = presenter.databaseModel ?: database

        return presenter
    }
}