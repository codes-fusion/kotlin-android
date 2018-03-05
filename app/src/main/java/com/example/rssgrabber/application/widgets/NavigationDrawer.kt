package com.example.rssgrabber.application.widgets

import android.animation.ValueAnimator
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle

class NavigationDrawer(private var mDrawerToggle: ActionBarDrawerToggle) {
    private var mToggleAnimator: ValueAnimator? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mActionBar: ActionBar? = null

    fun addSupportActionBar(actionBar: ActionBar?) {
        mActionBar = actionBar
    }

    fun addDrawerLayout(drawerLayout: DrawerLayout?) {
        mDrawerLayout = drawerLayout
        mDrawerLayout?.addDrawerListener(mDrawerToggle)
    }

    fun syncState() {
        mDrawerToggle.syncState()
    }

    /**
     * Setup navigation up with Back button
     * */
    fun setHomeUp(callback: () -> Unit) {
        mToggleAnimator?.cancel()
        mDrawerToggle.drawerArrowDrawable.setVerticalMirror(true)
        mDrawerToggle.setToolbarNavigationClickListener { callback() }

        mToggleAnimator = ValueAnimator.ofFloat(0.1f, 1.0f)
        mToggleAnimator?.apply {
            addUpdateListener { valueAnimator ->
                val slideOffset = valueAnimator.animatedValue as Float
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset)
                if (slideOffset > 0.9f) {
                    mDrawerToggle.isDrawerIndicatorEnabled = false
                    mActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
            duration = 200
            start()
        }
    }

    /**
     * Setup navigation with Hamburger button
     * */
    fun setHomeDefault() {
        mToggleAnimator?.cancel()
        mActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawerToggle.apply {
            isDrawerIndicatorEnabled = true
            drawerArrowDrawable.setVerticalMirror(false)
            drawerArrowDrawable.progress = 1.0f
            onDrawerSlide(mDrawerLayout, 1.0f)
        }

        mToggleAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        mToggleAnimator?.apply {
            addUpdateListener { valueAnimator ->
                val slideOffset = valueAnimator.animatedValue as Float
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset)
            }
            duration = 200
            start()
        }
    }
}