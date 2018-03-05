package com.example.rssgrabber.application.adapters

import android.content.Context

interface ViewAdapter<T> {
    companion object {
        val FOOTER = 1
        val ITEM = 2
    }

    fun getScrolledElements(): Int
    fun getList(): MutableList<T?>
    fun getItem(position: Int): T?
    fun setCallback(callBack: ViewAdapterListener)
    fun getCallback(): ViewAdapterListener?
    fun getWeakContext(): Context?
    fun destroy()
}