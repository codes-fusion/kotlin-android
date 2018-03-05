package com.example.rssgrabber.application.adapters

import android.view.View

interface ViewAdapterListener {
    fun onItemSelected(position: Int, view: View? = null)
}