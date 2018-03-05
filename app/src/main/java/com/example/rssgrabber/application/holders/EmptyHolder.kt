package com.example.rssgrabber.application.holders

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.rssgrabber.application.adapters.ViewAdapterImpl

class EmptyHolder(view: FrameLayout) : BaseHolder(view) {
    override fun render(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder, position: Int) {}

    fun createFrameLayout(viewGroup: ViewGroup): FrameLayout {
        val frameLayout = FrameLayout(viewGroup.context)
        frameLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frameLayout.setBackgroundColor(Color.WHITE)
        return frameLayout
    }
}