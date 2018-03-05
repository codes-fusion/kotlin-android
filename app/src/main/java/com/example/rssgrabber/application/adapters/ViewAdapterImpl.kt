package com.example.rssgrabber.application.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import com.example.rssgrabber.application.holders.BaseHolder
import com.example.rssgrabber.application.holders.EmptyHolder
import com.example.rssgrabber.application.holders.ItemHolder
import java.lang.ref.WeakReference

class ViewAdapterImpl<T> :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewAdapter<T> {

    private var lastPosition = -1
    private var mList: MutableList<T?> = mutableListOf()
    private var weakContext: WeakReference<Context>? = null
    private var mCallBack: ViewAdapterListener? = null

    fun createView(viewGroup: ViewGroup, layout: Int): View =
            LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false)

    private fun createFrameLayout(viewGroup: ViewGroup): FrameLayout {
        val frameLayout = FrameLayout(viewGroup.context)
        frameLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frameLayout.setBackgroundColor(Color.WHITE)
        return frameLayout
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) as ViewAdapterItem?
        return item?.adapterType ?: ViewAdapter.ITEM
    }

    override fun getList(): MutableList<T?> = mList

    override fun getScrolledElements(): Int {
        var count = 0
        if (itemCount <= 0) {
            return count
        }
        (0..itemCount - 1)
            .filter { getItem(it) != null }
            .map { getItem(it) as ViewAdapterItem? }
            .filter { it != null && it.adapterType != ViewAdapter.FOOTER }
            .forEach { count++ }
        return count
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        weakContext = WeakReference(viewGroup.context)
        when (viewType) {
            ViewAdapter.ITEM -> return ItemHolder(viewGroup)
            else -> return EmptyHolder(createFrameLayout(viewGroup))
        }
    }

    override fun getItem(position: Int): T? {
        if (position <= mList.size) {
            return mList[position]
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseHolder) {
            holder.render(this, holder, position)
            setAnimation(holder.itemView, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        if (holder is BaseHolder) {
            holder.onViewDetachedFromWindow(this, holder)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)
        if (holder is BaseHolder) {
            holder.onViewAttachedToWindow(this, holder)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            // val animation = AnimationUtils.loadAnimation(weakContext?.get(), android.R.anim.slide_in_left)
            val animation = AlphaAnimation(0.0f, 1.0f)
            animation.duration = 300
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun setCallback(callBack: ViewAdapterListener) {
        mCallBack = callBack
    }

    override fun getCallback(): ViewAdapterListener? = mCallBack
    override fun getItemCount(): Int = mList.size
    override fun getWeakContext(): Context? = weakContext?.get()
    override fun destroy() {
        mCallBack = null
    }
}