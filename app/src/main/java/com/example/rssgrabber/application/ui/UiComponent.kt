package com.example.rssgrabber.application.ui

import android.view.ViewGroup
import android.view.ViewManager
import com.wang.avi.AVLoadingIndicatorView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.ankoView

interface UiComponent : AnkoComponent<ViewGroup> {
    fun ViewManager.loadingIndicatorView(theme: Int = 0, init: AVLoadingIndicatorView.() -> Unit) = ankoView(::AVLoadingIndicatorView, theme, init)
    fun createAnko(viewGroup: ViewGroup) = createView(AnkoContext.create(viewGroup.context, viewGroup))
}