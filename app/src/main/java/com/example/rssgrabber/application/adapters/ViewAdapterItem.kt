package com.example.rssgrabber.application.adapters

open class ViewAdapterItem {
    var adapterType: Int = 0
    var adapterId: Float = 0.0f
    var color: String? = null

    fun asItem(): ViewAdapterItem {
        adapterType = ViewAdapter.ITEM
        return this
    }

    fun asFooter(): ViewAdapterItem {
        adapterType = ViewAdapter.FOOTER
        return this
    }
}