package com.example.rssgrabber.utils

import android.view.View
import android.view.animation.Animation
import android.widget.TextView

class ToolbarUtils(
        val view: View?,
        val enterAnimation: Animation?,
        val exitAnimation: Animation?) {

    fun flipTextView(title: String?) {
        val textView = view as TextView?
        val isEmpty = textView?.text?.isEmpty() ?: true
        enterAnimation?.reset()
        exitAnimation?.reset()

        if (isEmpty) {
            textView?.text = title
            textView?.startAnimation(enterAnimation)
        } else {
            textView?.startAnimation(exitAnimation)
            exitAnimation?.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    textView?.text = title
                    textView?.startAnimation(enterAnimation)
                }
            })
        }
    }
}