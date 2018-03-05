package com.example.rssgrabber.application.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.rssgrabber.application.adapters.ViewAdapterImpl

open class BaseHolder(var view: View) : RecyclerView.ViewHolder(view) {
    open fun render(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder, position: Int) {}
    open fun onViewDetachedFromWindow(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder?) {}
    open fun onViewAttachedToWindow(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder?) {}
}