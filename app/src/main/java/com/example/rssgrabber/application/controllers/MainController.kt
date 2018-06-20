package com.example.rssgrabber.application.controllers

import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rssgrabber.Backend
import com.example.rssgrabber.R
import com.example.rssgrabber.application.adapters.ViewAdapterImpl
import com.example.rssgrabber.application.adapters.ViewAdapterItem
import com.example.rssgrabber.application.adapters.ViewAdapterListener
import com.example.rssgrabber.application.presenters.FeedPresenter
import com.example.rssgrabber.application.ui.UiMainController
import com.example.rssgrabber.modules.ListModule
import com.example.rssgrabber.retrofit.DataRequest
import com.example.rssgrabber.retrofit.DataResponse
import com.example.rssgrabber.retrofit.FeedData
import com.example.rssgrabber.utils.AndroidUtils
import com.wang.avi.AVLoadingIndicatorView
import javax.inject.Inject

class MainController : BaseController(),
    com.example.rssgrabber.application.View, ViewAdapterListener {

    private var mRefreshLayout: SwipeRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mPreloader: AVLoadingIndicatorView? = null

    @Inject
    lateinit var adapter: ViewAdapterImpl<ViewAdapterItem>

    @Inject
    lateinit var utils: AndroidUtils

    @Inject
    lateinit var presenter: FeedPresenter

    init {
        retainViewMode = RetainViewMode.RELEASE_DETACH
    }

    private fun loadFeed(forceUpdate: Boolean = false) {
        val request = DataRequest(url= Backend.RSS)
        val started = presenter.fetchFeedList(request, forceUpdate)
        if (started && !forceUpdate) {
            activityBridge?.setToolbarTitle(activity?.getString(R.string.loading))
            mPreloader?.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = UiMainController().createAnko(container)

        mRefreshLayout = view.findViewById<SwipeRefreshLayout?>(R.id.refreshLayout)
        mRecyclerView = view.findViewById<RecyclerView?>(R.id.list)
        mPreloader = view.findViewById<AVLoadingIndicatorView>(R.id.preloader)
        mPreloader?.hide()

        val layoutManager = GridLayoutManager(activity, 1)

        activityBridge
            ?.getComponent()
            ?.listComponent(ListModule(activity as FragmentActivity, getTag()))
            ?.inject(this)

        layoutManager.isAutoMeasureEnabled = true
        layoutManager.generateDefaultLayoutParams()

        mRefreshLayout?.isRefreshing = false
        mRefreshLayout?.setOnRefreshListener(this)

        mRecyclerView?.adapter = adapter
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.setHasFixedSize(true)

        adapter.setCallback(this)
        
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        super.observeActivity()
        loadFeed()
        presenter.observeFeed(this, Observer { item ->
            mRefreshLayout?.isRefreshing = false
            (item as DataResponse?)?.data?.let {
                mPreloader?.hide()
                val feed = it as FeedData?
                feed?.channel?.let {
                    activityBridge?.setToolbarTitle(it.siteTitle)
                    activityBridge?.setHeader(it.title ?: "", it.description ?: "")
                    activityBridge?.setNavigationItems(it.divisions?.split(",") ?: listOf())

                    adapter.getList().clear()
                    it.feedItems?.forEach {
                        adapter.getList().add(it.asItem())
                    }

                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onItemSelected(position: Int, view: View?) {}

    override fun onRefresh() {
        loadFeed(true)
    }

    override fun onLifeCycleDestroy() {
        super.removeObservingActivity()
        adapter.destroy()
    }

    override fun showError(dataResponse: DataResponse) {}
    override fun hideError() {}

    override fun onDestroy() {
        super.onDestroy()
        appBridge?.disposeNetworkState(javaClass.name)
    }
}