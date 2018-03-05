package com.example.rssgrabber.application.ui

import android.app.Activity
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.util.TypedValue
import android.view.ViewManager
import com.example.rssgrabber.R
import com.wang.avi.AVLoadingIndicatorView
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.drawerLayout

class UIMainActivity {
    fun ViewManager.loadingIndicatorView(theme: Int = 0, init: AVLoadingIndicatorView.() -> Unit) = ankoView(::AVLoadingIndicatorView, theme, init)

    fun ViewManager.content(ui: Activity) = coordinatorLayout {
        lparams(width = matchParent, height = matchParent)

        themedAppBarLayout(R.style.AppTheme_AppBarOverlay) {
            toolbar {
                id = R.id.toolbar
                popupTheme = R.style.AppTheme_PopupOverlay
                backgroundResource = R.color.colorPrimary
                clipToPadding = false
                setTitleTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            }.lparams(width = matchParent) {
                val tv = TypedValue()
                if (ui.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                    height = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics);
                }
            }
        }.lparams(width = matchParent)

        constraintLayout {
            frameLayout { id = R.id.container }
        }.lparams(width = matchParent, height = matchParent) {
            behavior = AppBarLayout.ScrollingViewBehavior()
        }
    }

    fun createView(ui: Activity) = with(ui) {
        drawerLayout {
            id = R.id.drawer_layout
            fitsSystemWindows = true

            navigationView {
                id = R.id.nav_view
                fitsSystemWindows = true
                inflateHeaderView(R.layout.nav_header_main)
                inflateMenu(R.menu.activity_main_drawer)
            }.lparams(height = matchParent, gravity = GravityCompat.START)
        }
    }
}