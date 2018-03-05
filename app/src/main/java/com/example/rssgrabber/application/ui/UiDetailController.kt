package com.example.rssgrabber.application.ui

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.rssgrabber.R
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent

class UiDetailController : UiComponent {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        coordinatorLayout {
            id = R.id.refreshLayout
            backgroundColor = Color.GRAY
            layoutParams = with(LinearLayout.LayoutParams(matchParent, matchParent)) {
                width = matchParent
                height = matchParent
                this
            }

            loadingIndicatorView {
                id = R.id.preloader
                setIndicator("BallGridPulseIndicator")
                setIndicatorColor(Color.DKGRAY)
            }.lparams(width = dip(60), height = dip(60)) {
                gravity = Gravity.CENTER
            }
        }
    }
}