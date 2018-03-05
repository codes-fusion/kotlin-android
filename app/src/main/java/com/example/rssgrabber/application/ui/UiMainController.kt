package com.example.rssgrabber.application.ui

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.rssgrabber.R
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class UiMainController : UiComponent {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        swipeRefreshLayout {
            id = R.id.refreshLayout
            layoutParams = with(LinearLayout.LayoutParams(matchParent, matchParent)) {
                width = matchParent
                height = matchParent
                this
            }

            coordinatorLayout {
                lparams(width = matchParent, height = matchParent)

                recyclerView {
                    id = R.id.list
                    lparams(width = matchParent, height = matchParent)
                    clipToPadding = false
                }

                loadingIndicatorView {
                    id = R.id.preloader
                    setIndicator("BallGridPulseIndicator")
                    setIndicatorColor(Color.DKGRAY)
                    hide()
                }.lparams(width = dip(60), height = dip(60)) {
                    gravity = Gravity.CENTER
                }
            }
        }
    }
}