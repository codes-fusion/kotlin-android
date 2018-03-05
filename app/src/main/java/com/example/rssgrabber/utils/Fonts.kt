package com.example.rssgrabber.utils

import android.content.Context
import android.graphics.Typeface

class Fonts(context: Context) {
    private var fonts: MutableMap<Int, Typeface>? = null

    companion object {
        const val NORMAL = 1
        const val BOLD = 2
        const val AWESOME = 3
    }

    init {
        val normalTypeface = Typeface.createFromAsset(context.assets, "fonts/RobotoRegular.ttf")
        val boldTypeface = Typeface.createFromAsset(context.assets, "fonts/RobotoMedium.ttf")
        val fontAwesome = Typeface.createFromAsset(context.assets, "fonts/FontAwesome.ttf")

        fonts = mutableMapOf()
        fonts?.put(NORMAL, normalTypeface)
        fonts?.put(BOLD, boldTypeface)
        fonts?.put(AWESOME, fontAwesome)
    }

    fun get(type: Int): Typeface? {
        return fonts?.get(type)
    }
}