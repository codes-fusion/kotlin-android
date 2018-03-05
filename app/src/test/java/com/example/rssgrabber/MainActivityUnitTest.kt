package com.example.rssgrabber

import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import com.example.rssgrabber.application.adapters.ViewAdapterImpl
import com.example.rssgrabber.application.adapters.ViewAdapterItem
import com.example.rssgrabber.application.controllers.MainController
import com.example.rssgrabber.application.interactors.FeedInteractor
import com.example.rssgrabber.application.models.FeedModelDataBase
import com.example.rssgrabber.modules.ListModule
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.DataResponse
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.retrofit.NetworkResourceSubscriber
import com.example.rssgrabber.utils.AndroidUtils
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.reactivestreams.Subscription
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import javax.inject.Inject

@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class MainActivityUnitTest {

    private val latch = CountDownLatch(1)
    private var activity: MainActivity? = null
    private var mUnitTestComponent: MainActivityUnitTestComponent? = null

    companion object {

        private val testScheduler = TestScheduler()
        private val immediate = object : Scheduler() {
            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        @BeforeClass @JvmStatic
        fun setupClass() {
            RxJavaPlugins.setNewThreadSchedulerHandler { _ -> Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { _ -> Schedulers.trampoline() }
            RxJavaPlugins.setSingleSchedulerHandler { _ -> Schedulers.trampoline() }
            RxJavaPlugins.setIoSchedulerHandler { _ -> Schedulers.trampoline() }
            RxAndroidPlugins.setMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }
    }

    private fun <T> any(): T {
        return Mockito.any<T>()
    }

    private fun mockSubscriber(): NetworkResourceSubscriber {
        val w = Mockito.mock(NetworkResourceSubscriber::class.java)
        Mockito.doAnswer { a: InvocationOnMock ->
            val s = a.getArgument<NetworkResourceSubscriber>(0)
            val method = s.javaClass.getDeclaredMethod("request")
            method.isAccessible = true
            method.invoke(s, Long.MAX_VALUE)
            null
        }.`when`(w).onSubscribe(any<Subscription>())

        return w
    }

    @Inject @JvmField
    var okHttpClient: OkHttpClient? = null

    @Inject @JvmField
    var adapter: ViewAdapterImpl<ViewAdapterItem>? = null

    @Inject @JvmField
    var utils: AndroidUtils? = null

    @Inject @JvmField
    var interactor: FeedInteractor? = null

    @Inject @JvmField
    var database: FeedModelDataBase? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()

        mUnitTestComponent = DaggerMainActivityUnitTestComponent.create()
        mUnitTestComponent
            ?.listComponent(ListModule(activity as FragmentActivity, MainController().getTag()))
            ?.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun shouldActivityNotBeNull() {
        assertNotNull(activity)
    }

    @Test
    @Throws(Exception::class)
    fun shouldHaveOkHttp() {
        assertNotNull(okHttpClient)
    }

    @Test
    @Throws(Exception::class)
    fun shouldHavePresenter() {
        assertNotNull(interactor)
    }

    @Test
    @Throws(Exception::class)
    fun feed() {
        assertNotNull(interactor)

        val request = DataRequest(url=Backend.RSS)
        val started = interactor?.fetchFeedList(request, true) ?: false

        assertTrue(started)

        interactor?.observeFeed(activity as FragmentActivity, Observer {
            val data = (it as DataResponse?)?.data
            assertNotNull(data)

            val feed = data as FeedData?
            assertNotNull(feed?.channel?.feedItems?.firstOrNull())
        })

        // latch.await()
    }
}