package com.example.rssgrabber.application.decorators

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler

class ScaleFadeChangeHandler : AnimatorChangeHandler(DEFAULT_ANIMATION_DURATION, true) {

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        val animator = AnimatorSet()
        to?.let {
            val start = (if (toAddedToContainer) 0f else it.alpha).toFloat()
            val animation = ObjectAnimator.ofFloat(to, View.ALPHA, start, 1f)
            animation.duration = 200
            animator.play(animation)
        }

        from?.let {
            animator.play(ObjectAnimator.ofFloat(from, View.ALPHA, 0f))
            animator.play(ObjectAnimator.ofFloat(from, View.SCALE_X, 0.8f))
            animator.play(ObjectAnimator.ofFloat(from, View.SCALE_Y, 0.8f))
        }

        return animator
    }

    override fun resetFromView(from: View) {}
}