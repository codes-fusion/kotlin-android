package com.example.rssgrabber.modules

import android.graphics.Typeface
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.TextView
import com.example.rssgrabber.MainActivity
import com.example.rssgrabber.R
import com.example.rssgrabber.commons.ToolbarScope
import com.example.rssgrabber.utils.ToolbarUtils
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.UI
import org.jetbrains.anko.textView
import javax.inject.Named

@Module
class ToolbarModule(val activity: MainActivity) {

    @ToolbarScope
    @Provides
    @Named("flipInAnimation")
    fun provideFlipInAnimation(): ScaleAnimation {
        return AnimationUtils.loadAnimation(activity, R.anim.flip_in) as ScaleAnimation
    }

    @ToolbarScope
    @Provides
    @Named("flipOutAnimation")
    fun provideFlipOutAnimation(): ScaleAnimation {
        return AnimationUtils.loadAnimation(activity, R.anim.flip_out) as ScaleAnimation
    }

    @ToolbarScope
    @Provides
    fun provideToolbarHandler(
            @Named("flipInAnimation") flipInAnimation: ScaleAnimation,
            @Named("flipOutAnimation") flipOutAnimation: ScaleAnimation): ToolbarUtils {

        activity.apply {
            activity.getToolbar()?.addView(UI { textView {
                id = R.id.toolbar_title
                textSize = 18f
                typeface = Typeface.DEFAULT_BOLD
                maxLines = 1
            } }.view)
        }

        return ToolbarUtils(activity.getToolbar()?.findViewById<TextView>(R.id.toolbar_title), flipInAnimation, flipOutAnimation)
    }
}